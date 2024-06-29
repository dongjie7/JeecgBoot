package job;

import com.alibaba.fastjson.JSON;
import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectReport;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.service.ITwProjectReportService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @description: 定时任务
 * @author: nbacheng
 * @create: 2023-01-14
 **/
@Slf4j
public class ScheduleTask implements Job {

    @Autowired
    ITwTaskService taskService;
    @Autowired
    ITwProjectService projectService;
    @Autowired
    ITwProjectReportService projectReportService;

    /**
     * 每天执行计算任务完成情况
     */
 
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<String> proCodeList = null;
		if (proCodeList == null) {
            List<TwProject> projects = projectService.lambdaQuery().select(TwProject::getId).list();
            proCodeList = projects == null ? null : projects.parallelStream().map(TwProject::getId).distinct().collect(Collectors.toList());
        }
        if (proCodeList != null) {
            proCodeList.forEach(pro -> {
                for (int i = -9; i <= -1; i++) {
                    LocalDate now = LocalDate.now().plusDays(i);
                    LocalDate date = now.plusDays(-1);
                    List<TwTask> list = taskService.lambdaQuery().eq(TwTask::getDeleted, 0).eq(TwTask::getProjectId, pro).lt(TwTask::getCreateTime, now).list();
                    Map<String, Object> map = new HashMap<>(8);
                    int task = 0;
                    int undoneTask = 0;
                    int baseLineList = 0;
                    if (list != null) {
                        task = list.size();
                        undoneTask = (int) list.stream().filter(o -> o.getDone() == 0).count();
                        baseLineList = (int) list.stream().filter(o -> o.getDone() == 0).filter(o -> {
                            if (ObjectUtils.isEmpty(o.getEndTime())) {
                                if (ObjectUtils.isNotEmpty(o.getCreateTime())) {
                                	LocalDate create = LocalDate.parse(DateUtil.daFormat(o.getCreateTime()));
                                    return create.plusDays(5).isAfter(now);
                                }
                                return true;
                            } else {
                            	LocalDate end = LocalDate.parse(DateUtil.daFormat(o.getEndTime()));
                                return end.plusDays(-1).isBefore(now);
                            }
                        }).count();
                    }
                    map.put("task", task);
                    map.put("undoneTask", undoneTask);
                    map.put("baseLineList", baseLineList);
                    String content = JSON.toJSONString(map);
                    
                    TwProjectReport build = new TwProjectReport();
                    build.setContent(content);
                    build.setUpdateTime(new Date());
                    TwProjectReport one = projectReportService.lambdaQuery().eq(TwProjectReport::getProjectId, pro).eq(TwProjectReport::getRepDate, date).one();
                    if (one != null) {
                    	build.setId(one.getId());
                        boolean update = projectReportService.updateById(build);
                        log.info("更新项目完成数量：{}", update);
                    } else {
                    	build.setCreateTime(new Date());
                    	build.setProjectId(pro);
                    	ZoneId zone = ZoneId.systemDefault();
                        Instant instant = date.atStartOfDay().atZone(zone).toInstant();
                    	build.setRepDate(Date.from(instant));
                        boolean save = projectReportService.save(build);
                        log.info("新增项目完成数量：{}", save);
                    }
                }

            });

        }
		
	}
}