package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.oa.util.DateUtil;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectLog;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.entity.TwTaskStages;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskStagesMapper;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.estar.tw.service.ITwCollectionService;
import org.jeecg.modules.estar.tw.service.ITwFileService;
import org.jeecg.modules.estar.tw.service.ITwProjectLogService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwSourceLinkService;
import org.jeecg.modules.estar.tw.service.ITwTaskMemberService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesService;
import org.jeecg.modules.estar.tw.service.ITwTaskTagService;
import org.jeecg.modules.estar.tw.service.ITwTaskToTagService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkTimeService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowService;
import org.jeecg.modules.estar.tw.util.Constant;
import org.jeecg.modules.estar.tw.util.StringUtils;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目任务表
 * @Author: nbacheng
 * @Date:   2023-07-01
 * @Version: V1.0
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TwTaskServiceImpl extends ServiceImpl<TwTaskMapper, TwTask> implements ITwTaskService {

	@Autowired
	ITwTaskMemberService taskMemberService;
	
	@Autowired
	ITwProjectService projectService;
	@Autowired
	ITwTaskStagesService taskStagesService;
	@Autowired
	ITwTaskWorkflowService taskWorkflowService;
	@Autowired
	ITwAccountService accountService;
	@Autowired
	ITwCollectionService collectionService;
	@Autowired
	ITwTaskWorkTimeService taskWorkTimeService;
	@Autowired
	ITwFileService fileService;
	@Autowired
	ITwSourceLinkService sourceLinkService;
	@Autowired
	ITwProjectLogService projectLogService;
	@Autowired
	ITwTaskTagService taskTagService;
	@Autowired
	ITwTaskToTagService taskToTagService;
	@Autowired
	TwTaskStagesMapper taskStagesMapper;
	@Resource
	private IEstarThirdService iEstarThirdService;
	
	
	@Override
	public TwTask getTaskById(String id) {
		 LambdaQueryWrapper<TwTask> taskQW = new LambdaQueryWrapper<>();
	        taskQW.eq(TwTask::getId, id);
	        return baseMapper.selectOne(taskQW);
	}

	@Override
	public List<Map> getTaskListByVersionAndDelete(Map params) {
		return baseMapper.selectTaskListByVersionAndDelete(params);
	}

	@Override
	@Transactional
	public Result<?> batchAssignTask(Map<String, Object> mmap) {
		 String taskIds = MapUtils.getString(mmap,"taskIds","[]");
	     String stageId = MapUtils.getString(mmap,"executorId");
	     JSONArray jsonArray = JSON.parseArray(taskIds);
	     for (Object obj : jsonArray) {
	        assignTask(String.valueOf(obj),stageId);
	     }
		return Result.OK("成功分配任务人员!");
	}
	
	@Transactional
    public Result<?> assignTask(String taskId,String executorId){
        if(StringUtils.isEmpty(taskId)){
        	throw new JeecgBootException("请选择任务！");
        }
        TwTask task = lambdaQuery().eq(TwTask::getId,taskId).one();
        if(ObjectUtils.isEmpty(task)){
        	throw new JeecgBootException("请选择任务！");
        }
        if(task.getDeleted()==1){
        	throw new JeecgBootException("任务在回收站中无法进行指派！");
        }
        return taskMemberService.inviteMember(executorId,taskId,1,0,false,false);
    }

	@Override
	@Transactional
	public Result<?> taskStagesSave(Map<String, Object> mmap) {
		String name = MapUtils.getString(mmap,"name");
        String projectId = MapUtils.getString(mmap,"projectId");
        if(StringUtils.isEmpty(name)){
            return Result.error("请填写列表名称！");
        }
        TwProject project = projectService.lambdaQuery().eq(TwProject::getId,projectId).eq(TwProject::getDeleted,0).one();
        if(ObjectUtils.isEmpty(project)){
            return Result.error("该项目已失效！");
        }
        Integer maxsort = selectMaxSortByProjectId(projectId);
        TwTaskStages taskStage = new TwTaskStages();
        taskStage.setProjectId(projectId);
        taskStage.setName(StringUtils.trim(name));
        taskStage.setTasksLoading(false);
        taskStage.setFixedCreator(false);
        taskStage.setShowTaskCard(false);
        taskStage.setTasks(new ArrayList());
        taskStagesService.save(taskStage);
        taskStage.setSort(maxsort==null?0:maxsort +1);
        taskStagesService.updateById(taskStage);
        return Result.OK(taskStage);
	}

	private Integer selectMaxSortByProjectId(String projectId) {
		return taskStagesMapper.selectMaxSortByProjectId(projectId);
	}

	@Override
	public Result<?> taskStagesEdit(Map<String, Object> mmap) {
		String name = MapUtils.getString(mmap,"name");
        String stageId = MapUtils.getString(mmap,"stageId");
        if(StringUtils.isEmpty(name)){
            return Result.error("请填写列表名称");
        }
        if(StringUtils.isEmpty(stageId)){
            return Result.error("请选择一个列表");
        }
        TwTaskStages taskStage = taskStagesService.lambdaQuery().eq(TwTaskStages::getId,stageId).one();
        if(ObjectUtils.isEmpty(taskStage)){
            return Result.error("该列表已失效！");
        }
        taskStagesService.lambdaUpdate().eq(TwTaskStages::getId,stageId).set(TwTaskStages::getName,name).update();
        return Result.OK("编辑任务列表成功！");
	}

	@Override
	public Result<?> taskStagesDel(Map<String, Object> mmap) {
		String id = MapUtils.getString(mmap,"code");
		TwTaskStages taskStage = taskStagesService.lambdaQuery().eq(TwTaskStages::getId,id).one();
        if(ObjectUtils.isEmpty(taskStage)){
        	return Result.error("该列表不存在！");
        }
        List<TwTask> tasks = lambdaQuery().eq(TwTask::getStageId,id).eq(TwTask::getDeleted,0).list();
        if(!CollectionUtils.isEmpty(tasks)){
        	return Result.error("请先清空此列表上的任务，然后再删除这个列表！");
        }
        taskStagesService.lambdaUpdate().eq(TwTaskStages::getId,id).remove();
		return Result.OK("删除任务列表成功！");
        
	}

	@Override
	public Result<?> recycleBatch(String stageId) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		String userId = loginUser.getUsername();
		TwTaskStages taskStage = taskStagesService.lambdaQuery().eq(TwTaskStages::getId,stageId).one();
        if(ObjectUtils.isEmpty(taskStage)){
        	return Result.error("任务列表不存在！");
        }
        List<TwTask> tasks = lambdaQuery().eq(TwTask::getStageId,stageId).eq(TwTask::getDeleted,0).list();
        if(!CollectionUtils.isEmpty(tasks)){
            for(TwTask task:tasks){
            	taskStagesService.taskHook(userId,task.getId(),"recycle","",0,
                        "","","",null,null);
            }
        }
        lambdaUpdate().eq(TwTask::getStageId,stageId).eq(TwTask::getDeleted,0)
                .set(TwTask::getDeleted,1).set(TwTask::getDeletedTime,new Date())
                .update();
        return Result.OK("批量回收成功！");
	}

	@Override
	public Result<?> taskSave(Map<String, Object> mmap) {
		String pid = MapUtils.getString(mmap, "pid");
        String name = MapUtils.getString(mmap, "name");
        String assign_to = MapUtils.getString(mmap, "assign_to");
        String stage_id = MapUtils.getString(mmap,"stage_id");
        String project_id = MapUtils.getString(mmap,"project_id");

        if(StringUtils.isEmpty(name)){
            return Result.error("请填写任务标题");
        }
        TwTask  task = new TwTask();
        task.setStageId(stage_id);
        task.setProjectId(project_id);
        if(StringUtils.isNotEmpty(pid)){
            Map parentTask = getTaskMapById(pid);
            if(MapUtils.isEmpty(parentTask)){
                return Result.error("父目录无效");
            }
            if(MapUtils.getInteger(parentTask,"deleted",-1) == 1){
                return Result.error("父任务在回收站中无法编辑");
            }
            if(MapUtils.getInteger(parentTask,"done",-1) == 1){
                return Result.error("父任务已完成，无法添加新的子任务");
            }
            task.setProjectId(MapUtils.getString(parentTask,"project_id"));
            task.setStageId(MapUtils.getString(parentTask,"stage_id"));
            task.setPid(pid);
        }
        task.setAssignTo(assign_to);
        task.setDescription("");
        task.setBeginTime(null);
        task.setEndTime(null);
        task.setName(name);
        return createTask(task,pid);
	}

	@Override
	@Transactional
	public Result<?> createTask(TwTask task, String pid) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		String userId = loginUser.getUsername();
		TwTaskStages ts = taskStagesService.getTaskStageById(task.getStageId());
        if(ObjectUtils.isEmpty(ts)){
            return Result.error("该任务列表无效");
        }
        TwProject project = projectService.getProjectByIdNotDel(task.getProjectId());
        if(ObjectUtils.isEmpty(project)){
            return Result.error("该项目已失效");
        }

        SysUser member = iEstarThirdService.getUserByUsername(task.getAssignTo()); 
        if(ObjectUtils.isEmpty(member)){
            return Result.error("任务执行人有误");
        }
        Map parentTask = null;
        if(StringUtils.isNotEmpty(pid)){
            parentTask = getTaskMapById(pid);
            if(ObjectUtils.isEmpty(parentTask)){
                return Result.error("父目录无效");
            }
            if(MapUtils.getInteger(parentTask,"deleted",-1) == 1){
                return Result.error("父任务在回收站中无法编辑");
            }
            if(MapUtils.getInteger(parentTask,"done",-1) == 1){
                return Result.error("父任务已完成，无法添加新的子任务");
            }
            task.setProjectId(MapUtils.getString(parentTask,"project_id"));
            task.setStageId(MapUtils.getString(parentTask,"stage_id"));
            task.setPid(pid);
        }

        Integer maxIdNum = baseMapper.selectMaxIdNumByProjectId(task.getProjectId());
        String path = "";
        if(maxIdNum == null)maxIdNum = 0;
        if(!ObjectUtils.isEmpty(parentTask)){
            String parentPath = MapUtils.getString(parentTask,"path");
            if(StringUtils.isNotEmpty(parentPath)){
                parentPath = ","+parentPath;
            }else{
                parentPath = "";
            }
            path = MapUtils.getString(parentTask,"id")+parentPath;
        }
        task.setPath(path);
        task.setPri(0);
        if(null == project.getOpenTaskPrivate() || 0 == project.getOpenTaskPrivate()){
            task.setPrivated(0);
        }else{
            task.setPrivated(1);
        }
        task.setIdNum(maxIdNum+1);
        int i = baseMapper.insert(task);
        taskStagesService.taskHook(userId,task.getId(),"create","",0,
                "","","",null,null);
        if(StringUtils.isNotEmpty(pid)){
        	taskStagesService.taskHook(userId,pid,"createChild","",0,
                    "","","",new HashMap(){{
                        put("taskName",task.getName());
                    }},null);
        }

        String logType = "inviteMember";
        Integer isExecutor = 0;

        if(StringUtils.isNotEmpty(task.getAssignTo())){
            if(task.getAssignTo().equals(userId)){
                logType="claim";
                isExecutor=1;
            }
            taskMemberService.inviteMember(task.getAssignTo(),task.getId(),1,isExecutor,false,false);
        }
        if(StringUtils.isEmpty(task.getAssignTo()) || isExecutor==1){
            taskMemberService.inviteMember(task.getAssignTo(),task.getId(),0,1,false,false);
        }

        if(i>0){
            Map taskMap = baseMapper.selectTaskById(task.getId());
            taskWorkflowService.queryRule(task.getProjectId(), task.getStageId(), task.getId(), null, 0);
            return Result.OK(taskStagesService.buildTaskMap(taskMap,task.getCreateBy()));
        }
        return Result.error("保存失败！");
	}

	@Override
	public Map getTaskMapById(String id) {
		return baseMapper.selectTaskById(id);
	}

	@Override
	public Result<?> assignTask(Map<String, Object> mmap) {
		String taskId = MapUtils.getString(mmap,"taskId");
        String executorId = MapUtils.getString(mmap,"executorId");
        if(StringUtils.isEmpty(taskId)){
            return Result.error("请选择任务");
        }
        return assignTask(taskId,executorId);
	}

	@Override
	public Result<?> getTaskMembers(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String taskId = MapUtils.getString(mmap,"taskId");
        Integer pageSize = MapUtils.getInteger(mmap,"pageSize");
        Integer page = MapUtils.getInteger(mmap,"page",1);
		IPage<Map> iPage = new Page<>();
        iPage.setCurrent(page);iPage.setSize(pageSize);
        IPage<Map> iPageResult = taskMemberService.getTaskMemberByTaskId(iPage,taskId);
        List<Map> resultList = new ArrayList<>();
        List<Map> records = iPageResult.getRecords();
        Map pc = null;
        if(!CollectionUtils.isEmpty(records)){
            String memberId = null;
            Map accountMap = null;
            String orgId = accountService.selectOrgIdByUserId(loginUser.getUsername());
            for(Map map:records){
            	memberId = MapUtils.getString(map,"member_id");
            	SysUser member = iEstarThirdService.getUserByUsername(memberId);
                accountMap = accountService.getMemberAccountByMemIdAndOrgId(memberId,orgId);
                Map returnEntity =  new HashMap<String, Object>();
                returnEntity.put("username", member.getUsername());
                returnEntity.put("name", member.getRealname());
                returnEntity.put("avatar", member.getAvatar());
                returnEntity.put("member_account_id",MapUtils.getString(accountMap,"user_id"));
                returnEntity.put("is_executor",MapUtils.getInteger(map,"is_executor"));
                returnEntity.put("is_owner",MapUtils.getInteger(map,"is_owner"));
                resultList.add(returnEntity);

            }

        }
        Map data = new HashMap();
        data.put("list",resultList);
        data.put("total",iPageResult.getTotal());
        data.put("page",iPageResult.getCurrent());
        return Result.OK(data);
	}

	@Override
	public IPage<Map> taskIndex(IPage<Map> page, Map<String, Object> mmap) {
		page = baseMapper.selectTaskListByParam(page,mmap);
        String memberId = MapUtils.getString(mmap,"memberId");
        List<Map> result = new ArrayList<>();
        List<Map> taskList = page.getRecords();
        if(!CollectionUtils.isEmpty(taskList)){
            taskList.stream().forEach(map -> {
                SysUser member = iEstarThirdService.getUserByUsername(MapUtils.getString(map,"assign_to"));
                if(!ObjectUtils.isEmpty(member)){
                    map.put("executor",new HashMap(){{
                        put("name",member.getRealname());
                        put("avatar",member.getAvatar());
                    }});
                }
                map = taskStagesService.buildTaskMap(map,memberId);
                result.add(map);
            });
        }
        page.setRecords(result);
        return page;
	}

	@Override
	public Result<?> recovery(String taskId) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
	    TwTask task = getTaskById(taskId);
        if(ObjectUtil.isEmpty(task)){
        	return Result.error("任务不存在");
        }
        if(task.getDeleted()==0){
        	return Result.error("任务已恢复");
        }
        lambdaUpdate().eq(TwTask::getId,taskId).set(TwTask::getDeleted,0).update();
        taskStagesService.taskHook(loginUser.getUsername(),taskId,"recovery","",0,
                "","","",null,null);
        return Result.OK("恢复成功");
	}

	@Override
	public Map getTaskByIdNoDel(String id) {
		return baseMapper.selectTaskByIdNoDel(id);
	}

	@Override
	@Transactional
	public void star(Map taskMap, String username, Integer starData) {
		String id = MapUtils.getString(taskMap,"id");
        Integer star = MapUtils.getInteger(taskMap,"star");
        if(1==starData){
            star = star+1;
        }else {
            star = star-1;
        }
        baseMapper.updateTaskStar(star,id);
        collectionService.starTask(id,username,star);
		
	}

	@Override
	public List<Map> taskSources(Map<String, Object> mmap) {
		String taskId = MapUtils.getString(mmap,"taskId");

        List<Map> sourceLinkList = sourceLinkService.getSourceLinkByLinkIdAndType(taskId,"task");
        List<Map> resultData = new ArrayList<>();
        if(!CollectionUtils.isEmpty(sourceLinkList)){
            Map file = null;
            TwProject project = null;
            for(Map map:sourceLinkList){
                file = fileService.getFileById(MapUtils.getString(map,"source_id"));
                project = projectService.getProjectById(MapUtils.getString(file,"project_id"));
                map.put("title",MapUtils.getString(file,"title"));
                file.put("fullName",MapUtils.getString(file,"title")+MapUtils.getString(file,"extension"));
                file.put("projectName",project.getName());
                map.put("sourceDetail",file);
                resultData.add(map);
            }
        }
        return resultData;
	}

	@Override
	public List<Map> taskWorkTimeList(Map<String, Object> mmap) {
		String taskId = MapUtils.getString(mmap,"taskId");
        List<Map> mapList = taskWorkTimeService.getTaskWorkTimeByTaskId(taskId);
        List<Map> recordResult = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mapList)){
            for(Map map : mapList){
            	SysUser sysuser = iEstarThirdService.getUserByUsername(MapUtils.getString(map,"member_id"));
            	Map member = new HashMap<String, Object>();
            	member.put("name", sysuser.getRealname());
            	member.put("avatar", sysuser.getAvatar());
                map.put("member", member);
                recordResult.add(map);
            }
        }
        return recordResult;
	}

	@Override
	public Map readTask(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String memberId = loginUser.getUsername();
        String taskId = MapUtils.getString(mmap,"taskId");
        Map task = baseMapper.selectTaskById(taskId);
        LambdaQueryWrapper<TwProject> projectQW = new LambdaQueryWrapper<>();
        projectQW.eq(TwProject::getId, MapUtils.getString(task,"project_id"));
        TwProject project = projectService.getBaseMapper().selectOne(projectQW);
        LambdaQueryWrapper<TwTaskStages> taskStageQW = new LambdaQueryWrapper<>();
        taskStageQW.eq(TwTaskStages::getId, MapUtils.getString(task,"stage_id"));
        TwTaskStages taskStage = taskStagesService.getBaseMapper().selectOne(taskStageQW);
        task.put("executor",null);
        if(StringUtils.isNotEmpty(MapUtils.getString(task,"assign_to"))){
            SysUser member = iEstarThirdService.getUserByUsername(MapUtils.getString(task,"assign_to"));
            task.put("executor",member);
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(task,"pid"))){
            TwTask pTask = baseMapper.selTaskById(MapUtils.getString(task,"pid"));
            task.put("parentTask",pTask);
            List<Map> pathList = new ArrayList<>();
            if(StringUtils.isNotEmpty(MapUtils.getString(task,"path"))){
                String path = MapUtils.getString(task,"path");
                String[] paths = path.split(",");
                for(int i=paths.length-1;i>=0;i--){
                    TwTask t = baseMapper.selTaskById(paths[i]);
                    int finalI = i;
                    pathList.add(new HashMap(){{
                        put("id",paths[finalI]);
                        put("name",t.getName());
                    }});
                }
            }
            task.put("parentTasks",pathList);
        }
        task.put("openBeginTime",project.getOpenBeginTime());
        task.put("projectName",project.getName());
        task.put("stageName",taskStage.getName());
        return taskStagesService.buildTaskMap(task,memberId);
	}

	@Override
	public void taskRecycle(String taskId, String memberId) {
		TwTask task = getTaskById(taskId);
        task.setDeleted(1);
        task.setDeletedTime(new Date());
        updateById(task);
        taskStagesService.taskHook(memberId,taskId,"recycle","",0,
                "","","",null,null);
	}

	@Override
	@Transactional
	public void edit(TwTask task, String memberId) {
		String type = null;
        if(StringUtils.isNotEmpty(task.getDescription()) || "<p><br></p>".equals(task.getDescription())){
            task.setDescription("");
            type = "clearContent";
        }
        updateById(task);

        if(StringUtils.isNotEmpty(task.getName())){
            type = "name";
        }
        if(StringUtils.isNotEmpty(task.getDescription())){
            type = "content";
        }
        if(!ObjectUtils.isEmpty(task.getPri())){
            type = "pri";
        }
        if(!ObjectUtils.isEmpty(task.getStatus())){
            type = "status";
        }
        if(!ObjectUtils.isEmpty(task.getBeginTime())){
            type = "setBeginTime";
        }
        if("".equals(task.getBeginTime())){
            type = "clearBeginTime";
        }
        if(!ObjectUtils.isEmpty(task.getEndTime())){
            type = "setEndTime";
        }
        if("".equals(task.getEndTime())){
            type = "clearEndTime";
        }
        if(!ObjectUtils.isEmpty(task.getWorkTime()) && task.getWorkTime()>0){
            type = "setWorkTime";
        }
        if(StringUtils.isNotEmpty(type)){
            String finalType = type;
            projectLogService.run(new HashMap(){{
                put("member_code",memberId);
                put("source_code",task.getProjectId());
                put("type", finalType);
                put("is_comment",0);
            }});
        }
	}

	@Override
	public Map taskLog(Map<String, Object> mmap) {
	      Integer pageSize = MapUtils.getInteger(mmap,"pageSize",1000);
	      Integer page = MapUtils.getInteger(mmap,"page",1);
	      String taskId = MapUtils.getString(mmap,"taskId");
	      Integer showAll = MapUtils.getInteger(mmap,"all",0);
	      Integer onlyComment = MapUtils.getInteger(mmap,"comment",0);
	      Integer is_comment = onlyComment>0?onlyComment:0;
	      IPage<TwProjectLog> pagel=Constant.createPage(new Page(),mmap);
	      List<TwProjectLog> records = new ArrayList<>();
	      if(0==showAll){
	          LambdaQueryChainWrapper<TwProjectLog> lqcw=projectLogService.lambdaQuery().eq(TwProjectLog::getSourceId,taskId).eq(TwProjectLog::getActionType,"task");
	          if(onlyComment>0){
	              lqcw.eq(TwProjectLog::getIsComment,1);
	          }
	          pagel=lqcw.orderByDesc(TwProjectLog::getId).page(pagel);
	          records = pagel.getRecords();
	      }else{
	          LambdaQueryChainWrapper<TwProjectLog> lqcw=projectLogService.lambdaQuery().eq(TwProjectLog::getSourceId,taskId).eq(TwProjectLog::getActionType,"task");
	          if(onlyComment>0){
	              lqcw.eq(TwProjectLog::getIsComment,1);
	          }
	          records=lqcw.orderByDesc(TwProjectLog::getId).list();
	      }

	      List<TwProjectLog> resultList = new ArrayList<>();
	      if(!CollectionUtils.isEmpty(records)){
	          SysUser member = null;
	          for(TwProjectLog pl : records){
	             if(pl.getIsRobot()>0 && "claim".equals(pl.getOpeType())){
	                 member = new SysUser();
	                 member.setRealname("PP Robot");
	                 pl.setMember(member);
	                 resultList.add(pl);
	                 continue;
	             }
	             member = iEstarThirdService.getUserByUsername(pl.getMemberId());
	             pl.setMember(member);
	              resultList.add(pl);
	          }
	      }
	      Map data = new HashMap();
	      data.put("list",resultList);
	      data.put("total",pagel.getTotal());
	      data.put("page",pagel.getCurrent());
	      return data;
	}

	@Override
	@Transactional
	public Result<?> createComment(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		String taskId = MapUtils.getString(mmap, "taskId");
	    String comment = MapUtils.getString(mmap, "comment");
	    String mentions = MapUtils.getString(mmap,"mentions");
	    if(StringUtils.isEmpty(taskId)){
	    	return Result.error("请选择任务");
        }
        TwTask task = lambdaQuery().eq(TwTask::getId,taskId).one();
        if(ObjectUtil.isEmpty(task)){
        	return Result.error("任务已失效！");
        }
        List<String> mentionList = new ArrayList<>();
        if(StringUtils.isNotEmpty(mentions)){
            JSONArray jsonArray = JSON.parseArray(mentions);
            for (Object obj : jsonArray) {
                mentionList.add(String.valueOf(obj));
            }
        }
        taskStagesService.taskHook(loginUser.getUsername(),taskId,"comment","",1,
                "",comment,"",new HashMap(){{
                    put("list",mentionList);
                }},null);
        return Result.OK("创建任务注解成功");
	}

	@Override
	public Result<?> edit(String taskId, Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
		String end_time = MapUtils.getString(mmap,"end_time",null);
        String description = MapUtils.getString(mmap,"description",null);
        Integer pri = MapUtils.getInteger(mmap,"pri",-1);
        String name = MapUtils.getString(mmap,"name",null);
        Integer sort = MapUtils.getInteger(mmap,"sort",-1);
        String begin_time = MapUtils.getString(mmap,"begin_time",null);
        String work_time = MapUtils.getString(mmap,"work_time",null);
        Integer status = MapUtils.getInteger(mmap,"status",-1);
        if(StringUtils.isEmpty(taskId)){
        	return Result.error("请选择一个任务");
        }
        TwTask task = lambdaQuery().eq(TwTask::getId,taskId).eq(TwTask::getDeleted,0).one();
        if(ObjectUtils.isEmpty(task)){
        	return Result.error("该任务在回收站中无法编辑");
        }
        if("<p><br></p>".equals(description)){
            description="";
        }
        LambdaUpdateChainWrapper<TwTask> luw = lambdaUpdate().eq(TwTask::getId,taskId);
        boolean updateMark = false;
        String type = "";
        if(null != name){
            luw = luw.set(TwTask::getName,name);
            updateMark = true;
            type="name";
        }
        if(null != description){
            luw = luw.set(TwTask::getDescription,description);
            updateMark = true;
            type="content";
            if("".equals(description)){
                type="clearContent";
            }
        }
        if(-1 != pri){
            luw = luw.set(TwTask::getPri,pri);
            updateMark = true;
            type="pri";
        }
        if(-1 != status){
            luw = luw.set(TwTask::getStatus,status);
            updateMark = true;
            type="status";
        }
        if(null != begin_time){
            luw = luw.set(TwTask::getBeginTime,begin_time);
            updateMark = true;
            type="setBeginTime";
            if("".equals(begin_time)){
                type="clearBeginTime";
            }
        }
        if(null != end_time){
            luw = luw.set(TwTask::getEndTime,end_time);
            updateMark = true;
            type="setEndTime";
            if("".equals(end_time)){
                type="clearEndTime";
            }
        }
        if(null != work_time){
            luw = luw.set(TwTask::getWorkTime,work_time);
            updateMark = true;
            type="setWorkTime";
        }

        if(-1 != sort){
            luw = luw.set(TwTask::getSort,sort);
            updateMark = true;
        }
        if(updateMark)luw.update();
        taskStagesService.taskHook(loginUser.getUsername(),taskId,type,"",0,
                "","","",null,null);
        return Result.OK("修改成功");

	}

	@Override
	public Result<?> dateTotalForProject(Map<String, Object> mmap) {
		String projectId = MapUtils.getString(mmap, "projectId");
	      String beginTime = MapUtils.getString(mmap, "beginTime");
	      String endTime = MapUtils.getString(mmap, "endTime");
	      Date now = new Date();
	      if(StringUtils.isEmpty(beginTime)){
	          beginTime=DateUtil.daFormat(DateUtil.add(now,5,-20));
	      }
	      if(StringUtils.isEmpty(endTime)){
	          endTime = DateUtil.daFormat(now);
	      }
	      List<String> dateList = DateUtil.findDaysStr(beginTime,endTime);
	      List<Map> mapList = new ArrayList<>();
	      dateList.stream().forEach(s -> {
	          String start = s + " 00:00:00";
	          String end = s + "23:59:59";
	          Integer total = getDateTaskTotalForProject(projectId,start,end);
	          mapList.add(new HashMap(){{
	              put("date",s);
	              put("total",total);
	          }});
	      });
	      return Result.OK(mapList);
	}

	private Integer getDateTaskTotalForProject(String projectId, String start, String end) {
		return baseMapper.selectDateTaskTotalForProject(projectId, start, end);
	}

	@Override
	public Result<?> taskToTags(Map<String, Object> mmap) {
		String taskId = MapUtils.getString(mmap,"taskCode");
	      List<Map> taskTagList = taskToTagService.getTaskToTagByTaskId(taskId);
	      List<Map> resultData = new ArrayList<>();
	      if(!CollectionUtils.isEmpty(taskTagList)){
	          Map taskTag = null;
	          for(Map map:taskTagList){
	              taskTag = taskTagService.getTaskTagById(MapUtils.getString(map,"tag_Id"));
	              map.put("tag",taskTag);
	              resultData.add(map);
	          }
	      }
	      return Result.OK(resultData);
	}

	@Override
	public Result<?> taskGantt(Map<String, Object> mmap) {
		String projectId = MapUtils.getString(mmap, "projectId");
		List<Map> listStagesGantt = taskStagesMapper.selectTaskStagesGanttByProjectId(projectId);
		List<Map> listTasksGantt = baseMapper.selectTaskGanttByProjectId(projectId);
		if (!CollectionUtils.isEmpty(listStagesGantt)) {
			if (!CollectionUtils.isEmpty(listTasksGantt)) {
				for (Map stagesmap : listStagesGantt) {
					for (Map tasksmap : listTasksGantt) {
						if (ObjectUtils.isEmpty(tasksmap.get("parent"))) {
							tasksmap.replace("parent", stagesmap.get("id"));
						}
					}
				}
				Map<String, Object> tasksmap = new HashMap<String, Object>();
				listStagesGantt.addAll(listTasksGantt);
				tasksmap.put("data", listStagesGantt);
				return Result.OK(tasksmap);
			} else {
				Map<String, Object> tasksmap = new HashMap<String, Object>();
				tasksmap.put("data", listStagesGantt);
				return Result.OK(tasksmap);
			}

		} else {
			return Result.error("获取不到数据");
		}
	}

}
