package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.tw.dto.ProjectDto;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectCollect;
import org.jeecg.modules.estar.tw.entity.TwProjectLog;
import org.jeecg.modules.estar.tw.entity.TwProjectMember;
import org.jeecg.modules.estar.tw.entity.TwProjectVersion;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.entity.TwTaskStages;
import org.jeecg.modules.estar.tw.entity.TwTaskStagesTemplate;
import org.jeecg.modules.estar.tw.mapper.TwProjectCollectMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMemberMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskStagesMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskStagesTemplateMapper;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.estar.tw.service.ITwProjectCollectService;
import org.jeecg.modules.estar.tw.service.ITwProjectLogService;
import org.jeecg.modules.estar.tw.service.ITwProjectMemberService;
import org.jeecg.modules.estar.tw.service.ITwProjectReportService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.util.Constant;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TwProjectServiceImpl extends ServiceImpl<TwProjectMapper, TwProject> implements ITwProjectService {

	@Autowired
	ITwAccountService accountService;
	@Autowired
	TwTaskStagesTemplateMapper taskStagesTemplateMapper;
	@Autowired
	TwTaskStagesMapper taskStagesMapper;
	@Autowired
	TwProjectMemberMapper projectMemberMapper;
	@Autowired
	ITwProjectMemberService projectMemberService;
	@Autowired
	TwProjectMapper projectMapper;
	@Autowired
	ITwProjectCollectService projectCollectService;
	@Autowired
	TwProjectCollectMapper projectCollectMapper;
	@Autowired
	TwTaskMapper taskMapper;
	@Autowired
	ITwProjectLogService projectLogService;
	//@Autowired
	//ITwTaskService taskService;
	@Autowired
	ITwProjectReportService projectReportService;
	 
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveProject(TwProject twProject) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		//保存项目
		save(twProject);
		//保存任务列表
		QueryWrapper<TwTaskStagesTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("project_template_id",twProject.getId());
		List<TwTaskStagesTemplate> list = taskStagesTemplateMapper.selectList(queryWrapper);
		if(ObjectUtil.isEmpty(list)) {
			list =  Constant.getDefaultTaskStageTemplate();
		}
		AtomicInteger i= new AtomicInteger(0);
		list.stream().forEach(t->{
			TwTaskStages taskStage = new TwTaskStages();
			taskStage.setProjectId(twProject.getId());
			taskStage.setName(t.getName());
			taskStage.setSort(i.get());
			taskStagesMapper.insert(taskStage);
			i.set(i.get() + 1);
		});
		//保存项目成员
		TwProjectMember projectMember = new TwProjectMember();
		projectMember.setProjectId(twProject.getId());
		projectMember.setIsOwner(1);
		projectMember.setUserId(userId);
		projectMemberMapper.insert(projectMember);
	}

	@Override
	public void recycle(String id) {
		projectMapper.updateRecycle(id, 1, new Date());
	}

	@Override
	public void recovery(String id) {
		projectMapper.updateRecycle(id, 0, new Date());
		
	}

	@Override
	public Result<?> collect(String id,String type) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        if(StringUtils.equals(type, "collect")) {
        	projectCollectService.lambdaUpdate().eq(TwProjectCollect::getProjectId,id).eq(TwProjectCollect::getUserId,userId).remove();
        	return Result.OK("取消收藏成功");
        }
        else {
        	if(projectCollectService.queryByProjectIdAndUserId(id, userId) != null ) {
        		return Result.OK("项目已经收藏，不能再次收藏");
        	}
        	else {
	        	TwProjectCollect projectCollect = new TwProjectCollect();
		        projectCollect.setProjectId(id);
		        projectCollect.setUserId(userId);
		        projectCollectService.save(projectCollect);
		        return Result.OK("项目收藏成功");
        	}
	        
        }
	}

	@Override
	public IPage<TwProject> queryCollectList(Page<TwProject> page, String userId) {
		List<TwProject> listproject = this.baseMapper.selectCollectProject(userId);
		return page.setRecords(listproject);
	}

	@Override
	public TwProject projectSet(String id) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		TwProject project = lambdaQuery().eq(TwProject :: getId ,id).one();
		if(ObjectUtils.isNotEmpty(project)) {
			project.setCollected(0);
			TwProjectCollect projectCollect = projectCollectService.lambdaQuery().eq(TwProjectCollect::getProjectId,project.getId())
		            .eq(TwProjectCollect :: getUserId,userId).one();
			if(ObjectUtils.isNotEmpty(projectCollect)) {
				project.setCollected(1);
			}
			TwProjectMember projectMember = projectMemberService.lambdaQuery().eq(TwProjectMember::getProjectId,project.getId())
	                .eq(TwProjectMember::getIsOwner,1).one();
			if(ObjectUtils.isNotEmpty(projectMember)) {
				SysUser user = iEstarThirdService.getUserByUsername(projectMember.getUserId());
				project.setOwnerName(user.getRealname());
				project.setOwnerAvatar(user.getAvatar());
			}
		}
		return project;
	}

	@Override
	public boolean quitProject(String id) {
		TwProject project = lambdaQuery().eq(TwProject :: getId ,id).one();
		if(ObjectUtils.isNotEmpty(project)) {
			TwProjectMember projectMember = projectMemberService.lambdaQuery().eq(TwProjectMember::getProjectId,project.getId())
	                .eq(TwProjectMember::getIsOwner,1).one();
			if(ObjectUtils.isNotEmpty(projectMember)) {
				if(projectMember.getIsOwner()<1) {
					projectMemberService.removeById(projectMember.getId());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void archive(String id) {
		projectMapper.updateArctive(id, 1, new Date());
		
	}

	@Override
	public void recoveryArchive(String id) {
		projectMapper.updateArctive(id, 0, null);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Result<?> querySelfList(ProjectDto projectDto) {

		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		
        Integer archive = projectDto.getArchive() == null ?-1:projectDto.getArchive();
        Integer type =  projectDto.getType() == null ?0:projectDto.getType();
        Integer delete = projectDto.getDeleted() == null ?-1:projectDto.getDeleted();
        String organizationId = projectDto.getOrganizationId() == null ?"":projectDto.getOrganizationId();
        String memberId = projectDto.getMemberId() == null ?"":projectDto.getMemberId();
        
        SysUser member = null;
        if(StringUtils.isNotEmpty(memberId)){
            member = iEstarThirdService.getUserByUsername(memberId) ;
        }else{
            member = iEstarThirdService.getUserByUsername(userId);
        }
        if(ObjectUtils.isEmpty(member)){
        	return Result.error("参数有误");
        }

        Integer deleted = delete == -1?1:delete;
        if(type == 0){
            deleted = 0;
        }

        IPage<Map> iPage = Constant.createPage(projectDto.getPageNo(),projectDto.getPageSize());

        Map params = new HashMap();
        params.put("memberId",userId);
        params.put("organizationId",accountService.selectOrgIdByUserId(userId));
        params.put("deleted",deleted);
        params.put("archive",archive);


        iPage = getMemberProjects(iPage,params);

        List<Map> resultList = new ArrayList<>();
        List<Map> records = iPage.getRecords();
        List<Map> pc = null;
        if(!CollectionUtils.isEmpty(records)){
            for(Map map:records){
                map.put("owner_name","-");
                pc = projectCollectService.getProjectCollection(MapUtils.getString(map,"id"),MapUtils.getString(map,"user_id"));
                if(pc!=null && pc.size()>0 && null!=pc.get(0).get("user_id")){
                    map.put("collected",1);
                }else{
                    map.put("collected",0);
                }
                Map pm = projectMemberService.gettMemberIdAndNameByProjectId(MapUtils.getString(map,"id"));
                if(MapUtils.isNotEmpty(pm)){
                    map.put("owner_name",pm.get("realname"));
                }
                resultList.add(map);
            }
        }
        iPage.setRecords(resultList);
		return Result.OK(iPage);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPage<Map> getMemberProjects(IPage<Map> page, Map params) {
		return baseMapper.selectMemberProjects(page,params);
	}

	@Override
	public TwProject getProjectById(String id) {
		LambdaQueryWrapper<TwProject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TwProject::getId, id);
        return baseMapper.selectOne(queryWrapper);
	}
	
	//根据projectCode获取project
    public Map getProjectByCode(String projectId){
        Map project = baseMapper.selectProjectById(projectId);
        project.put("privated",project.get("privated"));
        return project;
    }

	@Override
	public TwProject getProjectByIdNotDel(String id) {
		 LambdaQueryWrapper<TwProject> queryWrapper = new LambdaQueryWrapper<>();
	        queryWrapper.eq(TwProject::getId, id);
	        queryWrapper.eq(TwProject::getDeleted, 0);
	        return baseMapper.selectOne(queryWrapper);
	}

	@Override
	public Result<?> getLogBySelfProject(@RequestParam Map<String,Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		String orgId = accountService.selectOrgIdByUserId(loginUser.getUsername());
		String projectId = MapUtils.getString(mmap,"projectId");
        IPage<Map> ipage = Constant.createPage(mmap);
        Map params = new HashMap();
        params.put("memberId",loginUser.getUsername());
        params.put("orgId",orgId);
        params.put("projectId",projectId);

        IPage<Map> resultData =  getLogBySelfProject(ipage,params);

        if(null != resultData){
            if(StringUtils.isEmpty(projectId)){
                return Result.OK(resultData.getRecords());
            }else{
                return  Result.OK(Constant.createPageResultMap(ipage));
            }
        }
        return Result.OK();
	}
	
	public IPage<Map> getLogBySelfProject(IPage<Map> page,Map params){
        String projectId = MapUtils.getString(params,"projectId");
        if(StringUtils.isEmpty(projectId)){
            List<String> projectIds = baseMapper.selectProjectIdsByMemberAndOrg(params);
            if(CollectionUtils.isEmpty(projectIds))return page;
            page = baseMapper.selectTaskLogByProjectId(page,projectIds);
        }else{
            page = baseMapper.selectProjectLogByProjectId(page,projectId);

            List<Map> record = page.getRecords();
            List resultRecord = new ArrayList();
            if(Optional.ofNullable(record).isPresent()){
                record.stream().forEach(m->{
                    String action_type = MapUtils.getString(m,"action_type");
                    if("task".equals(action_type)){
                        m.put("sourceInfo",taskMapper.selectTaskById(MapUtils.getString(m,"source_id")));
                    }else if("project".equals(action_type)){
                        m.put("sourceInfo",projectMapper.selectProjectById(MapUtils.getString(m,"source_id")));
                    }
                    resultRecord.add(m);
                });
            }
            page.setRecords(resultRecord);
        }
        return page;
    }

	@Override
	public Result<?> projectStats(String projectId) {
        if(StringUtils.isEmpty(projectId)){
            return Result.error("该项目已失效");
        }
        Map projectMap = getProjectByProjectId(projectId);
        if(MapUtils.isEmpty(projectMap)){
            return Result.error("该项目已失效");
        }


        //List<TwTask> listTask = taskService.lambdaQuery().eq(TwTask::getDeleted,0).eq(TwTask::getProjectId,projectId).list();
        LambdaQueryWrapper<TwTask> taskQW = new LambdaQueryWrapper<>();
        taskQW.eq(TwTask::getDeleted,0).eq(TwTask::getProjectId,projectId);
        List<TwTask> listTask = taskMapper.selectList(taskQW);
        if(CollectionUtils.isEmpty(listTask)){
            listTask = new ArrayList<TwTask>();
        }
        Date now = new Date();
        String today = DateUtil.dateFormat(now);
        String tomorrow = DateUtil.dateFormat(DateUtil.add(now,5,-1));
        String nowTime = DateUtil.dateFormat(now);
        Integer total=0;
        final Integer[] unDone= {0};
        final Integer[] done= {0};
        final Integer[] overdue= {0};
        final Integer[] toBeAssign = {0};
        final Integer[] expireToday={0};
        final Integer[]  doneOverdue= {0};
        listTask.stream().forEach(task -> {
            if(StringUtils.isEmpty(task.getAssignTo())){
                toBeAssign[0]++;
            }
            if(ObjectUtil.isNotEmpty(task.getDone()) && task.getDone()>0){
                done[0] ++;
            }else{
                unDone[0] ++;
            }
            if(ObjectUtil.isNotEmpty(task.getEndTime())){
                if(ObjectUtil.isNotEmpty(task.getDone()) && task.getDone()==0){
                    if(DateUtil.dateFormat(task.getEndTime()).compareTo(tomorrow) == -1 && DateUtil.dateFormat(task.getEndTime()).compareTo(today) >=0){
                        doneOverdue[0] ++;
                    }
                    if(-1 == DateUtil.dateFormat(task.getEndTime()).compareTo(nowTime)){
                        overdue[0]++;
                    }
                    String endTime = ObjectUtil.isNotEmpty(task.getEndTime())&&DateUtil.daFormat(task.getEndTime()).length()>=10?DateUtil.daFormat(task.getEndTime()).substring(0,10):"";
                    if(endTime.compareTo(DateUtil.daFormat(now)) == 0){
                        expireToday[0] ++;
                    }
                }else{
                    List<TwProjectLog> logList = projectLogService.lambdaQuery().eq(TwProjectLog::getActionType,"task")
                            .eq(TwProjectLog::getSourceId,task.getId()).eq(TwProjectLog::getOpeType,"done").list();
                    if(!CollectionUtils.isEmpty(logList)){
                        if(task.getEndTime().compareTo(logList.get(0).getCreateTime()) == -1){
                            doneOverdue[0]++;
                        }
                    }
                }
            }
        });
        Map data = new HashMap();
        data.put("total", listTask.size());
        data.put("unDone",unDone[0]);
        data.put("done",done[0]);
        data.put("overdue",overdue[0]);
        data.put("toBeAssign", toBeAssign[0]);
        data.put("expireToday",expireToday[0]);
        data.put("doneOverdue",doneOverdue[0]);
        return Result.OK(data);
	}

	//根据projectId获取project
    public Map getProjectByProjectId(String projectId){
        Map project = baseMapper.selectProjectById(projectId);
        project.put("privated",project.get("privated"));
        return project;
    }
	
	@Override
	public Result<?> getProjectReport(String projectId) {
        if(StringUtils.isEmpty(projectId)){
            return Result.error("项目已失效");
        }
        return Result.OK(projectReportService.getReportByDay(projectId,10));
	}




}
