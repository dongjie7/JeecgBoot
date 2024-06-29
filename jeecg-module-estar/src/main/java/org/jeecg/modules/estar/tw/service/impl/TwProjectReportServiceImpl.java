package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.tw.entity.TwProjectReport;
import org.jeecg.modules.estar.tw.mapper.TwProjectReportMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目端节点表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwProjectReportServiceImpl extends ServiceImpl<TwProjectReportMapper, TwProjectReport> implements ITwProjectReportService {

	/**
    *
    * 计算最近n天的数据
    * @param String $projectId 项目id
    * @param Integer day 近n天
    * @param projectId 项目id
    * @param day         近n天
    */
	@Override
	public Map getReportByDay(String projectId, Integer day) {
		Map<String, Object> result = new HashMap<>();
        LocalDate now = LocalDate.now();
        List<String> date = new ArrayList<>();
        List<Integer> task = new ArrayList<>();
        List<Integer> undoneTask = new ArrayList<>();
        List<Integer> baseLineList = new ArrayList<>();
        List<LocalDate> dateList = Stream.iterate(now, o -> o.plusDays(-1)).limit(day).collect(Collectors.toList());
        List<TwProjectReport> projectReports = lambdaQuery().in(TwProjectReport::getRepDate, dateList).eq(TwProjectReport::getProjectId, projectId)
                .orderByAsc(TwProjectReport::getRepDate).list();
        if (projectReports != null) {
            projectReports.forEach(o -> {
                date.add((DateUtil.daFormat(o.getRepDate())).substring(5));
                Map<String, Object> map = JSONObject.parseObject(o.getContent());
                task.add((int) map.get("task"));
                undoneTask.add((int) map.get("undoneTask"));
                baseLineList.add((int) map.get("baseLineList"));
            });
        }
        result.put("date", date);
        result.put("task", task);
        result.put("undoneTask", undoneTask);
        result.put("baseLineList", baseLineList);
        return result;
	}

}
