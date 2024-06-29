package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.entity.TwTaskMember;
import org.jeecg.modules.estar.tw.entity.TwTaskStages;
import org.jeecg.modules.estar.tw.mapper.CommMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMemberMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskStagesMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskTagMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskToTagMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectLogService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwProjectVersionService;
import org.jeecg.modules.estar.tw.service.ITwTaskMemberService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowService;
import org.jeecg.modules.estar.tw.util.Constant;
import org.jeecg.modules.estar.tw.util.StringUtils;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;

import cn.hutool.core.util.ObjectUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectLog;

/**
 * @Description: 任务列表
 * @Author: nbacheng
 * @Date: 2023-05-29
 * @Version: V1.0
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TwTaskStagesServiceImpl extends ServiceImpl<TwTaskStagesMapper, TwTaskStages>
		implements ITwTaskStagesService {

	@Autowired
	CommMapper commMapper;
	//@Autowired
	//ITwProjectService projectService;
	@Autowired
	TwProjectMapper projectMapper;
	@Autowired
	TwTaskMapper taskMapper;
	//@Autowired
	//ITwTaskService taskService;
	@Autowired
	TwTaskToTagMapper taskToTagMapper;
	@Autowired
	TwTaskTagMapper taskTagMapper;
	@Autowired
	ITwTaskWorkflowService taskWorkflowService;
	//@Autowired
	//ITwTaskMemberService taskMemberService;
	@Autowired
	TwTaskMemberMapper taskMemberMapper;
	@Autowired
	ITwProjectVersionService projectVersionService;
	
	//@Autowired
	//ITwTaskStagesService taskStagesService;
	@Autowired
	ITwProjectLogService projectLogService;
	@Resource
	private IEstarThirdService iEstarThirdService;
	@Resource
    private IFlowThirdService iFlowThirdService;

	@Override
	public Result<?> getStagesTasks(Map<String, Object> tasksmap) {
		String stageId = MapUtils.getString(tasksmap, "stageId", "");
		Integer done = MapUtils.getInteger(tasksmap, "done", -1);
		String title = MapUtils.getString(tasksmap, "title", "");
		String pri = MapUtils.getString(tasksmap, "pri", "");
		String stage = MapUtils.getString(tasksmap, "stage", "");
		String status = MapUtils.getString(tasksmap, "status", "");
		String executor = MapUtils.getString(tasksmap, "executor", "");
		String creator = MapUtils.getString(tasksmap, "creator", "");
		String joiner = MapUtils.getString(tasksmap, "joiner", "");
		String endTime = MapUtils.getString(tasksmap, "endTime", "");
		String beginTime = MapUtils.getString(tasksmap, "beginTime", "");
		String createTime = MapUtils.getString(tasksmap, "createTime", "");
		String doneTime = MapUtils.getString(tasksmap, "doneTime", "");
		if (StringUtils.isEmpty(stageId)) {
			return Result.error("数据解析异常");
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.* from tw_task t ");
		StringBuffer where = new StringBuffer();
		where.append(" where t.pid = '' ");
		where.append(" and t.deleted = 0 ");
		if (-1 != done) {
			where.append(" and  done = " + done);
		}
		if (stageId.startsWith("[")) {
			where.append(whereBuild(stageId, "t.stage_id", " and "));
		} else {
			where.append(" and t.stage_id='" + stageId + "' ");
		}
		if (StringUtils.isNotEmpty(pri) && pri.startsWith("[")) {
			where.append(whereBuild(pri, "t.pri", " and "));
		} else if (StringUtils.isNotEmpty(pri)) {
			where.append(" and t.pri=" + pri);
		}
		if (StringUtils.isNotEmpty(status) && status.startsWith("[")) {
			where.append(whereBuild(status, "t.status", " and "));
		} else if (StringUtils.isNotEmpty(status)) {
			where.append(" and t.status=" + status);
		}

		if (StringUtils.isNotEmpty(title)) {
			where.append(" and  t.name like '%" + title + "%'");
		}
		if (StringUtils.isNotEmpty(endTime) && !"[]".equals(endTime)) {
			String time = endTime.replace("\"", "").replace("[", "").replace("]", "");
			where.append(" and  t.end_time between '" + time.split(",")[0] + "' and '" + time.split(",")[1] + "' ");
		}
		if (StringUtils.isNotEmpty(beginTime) && !"[]".equals(beginTime)) {
			String time = beginTime.replace("\"", "").replace("[", "").replace("]", "");
			where.append(" and  t.begin_time between '" + time.split(",")[0] + "' and '" + time.split(",")[1] + "' ");
		}
		if (StringUtils.isNotEmpty(createTime) && !"[]".equals(createTime)) {
			String time = createTime.replace("\"", "").replace("[", "").replace("]", "");
			where.append(" and t.create_time between '" + time.split(",")[0] + "' and '" + time.split(",")[1] + "' ");
		}
		if (StringUtils.isNotEmpty(doneTime) && !"[]".equals(doneTime)) {
			sql.append(" left join tw_project_log pl on t.id = pl.source_id ");
			where.append(" and pl.action_type='task' and pl.ope_type='done' ");
			String time = createTime.replace("\"", "").replace("[", "").replace("]", "");
			where.append(" and pl.create_time between '" + time.split(",")[0] + "' and '" + time.split(",")[1] + "' ");
		}
		boolean joinTaskMember = false;
		if (StringUtils.isNotEmpty(executor) && !"[]".equals(executor)) {
			joinTaskMember = true;
			sql.append(" left join tw_task_member tm on t.id=tm.task_id");
			where.append(whereBuild(executor, " tm.member_id ", " and "));
			where.append(" and tm.is_executor=1 ");
		}
		boolean creatorTaskMember = false;
		if (StringUtils.isNotEmpty(creator) && !"[]".equals(creator)) {
			if (!joinTaskMember) {
				creatorTaskMember = true;
				sql.append(" left join tw_task_member tm on t.id=tm.task_id ");
				where.append(whereBuild(creator, " tm.member_id ", " and "));
				where.append(" and tm.is_executor = 1 ");
			}
		}
		if (StringUtils.isNotEmpty(joiner) && !"[]".equals(joiner)) {
			if (!joinTaskMember) {
				if (!creatorTaskMember) {
					sql.append(" left join tw_task_member tm on t.id=tm.task_id ");
				}
				where.append(whereBuild(joiner, " tm.member_id ", " and "));
			}
		}

		SysUser loginUser = iEstarThirdService.getLoginUser();
		String userId = loginUser.getUsername();
		List<Map> resultList = new ArrayList<>();
		String sqlResult = sql.toString() + where.toString();
		List<Map> mapList = commMapper.customQueryItem(sqlResult);
		if (!CollectionUtils.isEmpty(mapList)) {
			Map executorMap = null;
			List<Map> tags = null;
			Map taskMemberMap = new HashMap();
			for (Map map : mapList) {
				String assign_to = MapUtils.getString(map, "assign_to");
				if (ObjectUtil.isNotEmpty(assign_to)) {
					if (ObjectUtil.isNotEmpty(MapUtils.getObject(taskMemberMap, assign_to))) {
						map.put("executor", MapUtils.getObject(taskMemberMap, assign_to));
					} else {
						SysUser member = iEstarThirdService.getUserByUsername(MapUtils.getString(map, "assign_to"));
                        if(ObjectUtil.isNotEmpty(member)) {
                            executorMap = new HashMap();
						    executorMap.put("name", member.getRealname());
						    executorMap.put("avatar", member.getAvatar());
						    map.put("executor", executor);
						    taskMemberMap.put(assign_to, executorMap);
                        }
						
					}
				}
				map = buildTaskMap(map, userId);
				resultList.add(map);
			}
		}
		return Result.OK(resultList);
	}

	private String whereBuild(String json, String field, String gl) {
		StringBuffer where = new StringBuffer();
		JSONArray jsonArray = JSON.parseArray(json);
		if (StringUtils.isNotEmpty(jsonArray)) {
			where.append(field + " in(");
			for (int i = 0; i < jsonArray.size(); i++) {
				where.append("'" + String.valueOf(jsonArray.get(i)) + "'");
				if (i < jsonArray.size() - 1) {
					where.append(",");
				}
			}
			where.append(")");
		}
		if (StringUtils.isNotEmpty(where.toString())) {
			return " and " + where.toString();
		}
		return "";
	}

	@Override
	public Map buildTaskMap(Map task, String memberId) {
		String taskId = MapUtils.getString(task, "id");
		task.put("priText", getPriTextAttr(MapUtils.getString(task, "pri")));
		task.put("statusText", getStatusTextAttr(MapUtils.getString(task, "status")));
		task.put("liked", getLikedAttr(taskId, memberId));
		task.put("stared", getStaredAttr(taskId, memberId));
		task.put("tags", getTagsAttr(taskId));
		task.put("childCount", getChildCountAttr(taskId));
		task.put("hasUnDone", getHasUnDoneAttr(taskId));
		task.put("parentDone", getParentDoneAttr(MapUtils.getString(task, "pid")));
		task.put("hasComment", getHasCommentAttr(taskId));
		task.put("hasSource", getHasSourceAttr(taskId));
		task.put("canRead", getCanReadAttr(taskId, memberId, MapUtils.getInteger(task, "privated")));
		return task;
	}

	protected String getPriTextAttr(String pri) {
		@SuppressWarnings("serial")
		Map<String, String> status = new HashMap() {
			{
				put("0", "普通");
				put("1", "紧急");
				put("2", "非常紧急");
			}
		};
		if (StringUtils.isEmpty(pri)) {
			pri = "0";
		}
		return status.get(pri);
	}

	protected String getStatusTextAttr(String stat) {
		@SuppressWarnings({ "serial" })
		Map<String, String> status = new HashMap() {
			{
				put("0", "未开始");
				put("1", "已完成");
				put("2", "进行中");
				put("3", "挂起");
				put("4", "测试中");
			}
		};
		if (StringUtils.isEmpty(stat)) {
			stat = "0";
		}
		return status.get(stat);
	}

	protected Integer getLikedAttr(String id, String memberId) {
		Integer like = 0;
		Map taskLike = baseMapper.selectTaskLike(id, memberId);
		if (MapUtils.isNotEmpty(taskLike)) {
			like = 1;
		}
		return like;
	}

	protected Integer getStaredAttr(String id, String memberId) {
		Integer stared = 0;
		Map taskStar = baseMapper.selectTaskStared(id, memberId);
		if (MapUtils.isNotEmpty(taskStar)) {
			stared = 1;
		}
		return stared;
	}
	
	public Integer getParentDoneAttr(String id){
        Integer done = 1;
        //TwTask parentDone = taskService.lambdaQuery().eq(TwTask::getId,id).one();
        LambdaQueryWrapper<TwTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TwTask::getId,id);
        TwTask parentDone = taskMapper.selectOne(queryWrapper);
        if(ObjectUtil.isNotEmpty(parentDone) && parentDone.getDeleted()==0&& parentDone.getDone() ==0){
            done = 0;
        }

        return done;
    }

    public Integer getHasUnDoneAttr(String id){
        Integer hasUnDone = 0;
        Map parentDone = baseMapper.selectHasUnDone(id);
        Integer tp_count = MapUtils.getInteger(parentDone,"tp_count",0);
        if(tp_count>0){
            hasUnDone = 1;
        }else{
            hasUnDone = 0;
        }
        return hasUnDone;
    }

    public Integer getHasCommentAttr(String id){
        Map hasComment = baseMapper.selectHasComment(id);
        if(!MapUtils.isEmpty(hasComment)){
            return MapUtils.getInteger(hasComment,"tp_count");
        }else{
            return 0;
        }
    }
	
	protected Integer getHasSourceAttr(String id){
        Map hasSource = baseMapper.selectHasSource(id);
        if(!MapUtils.isEmpty(hasSource)){
            return MapUtils.getInteger(hasSource,"tp_count");
        }else{
            return 0;
        }
    }
	
	/**
     * 标签
     */
	
	@Override
	public List<Map> getTagsAttr(String taskId){
        List<Map> tags = new ArrayList();
        List<Map> result = new ArrayList<>();
        if(StringUtils.isNotEmpty(taskId)){
            tags = taskToTagMapper.selectTaskToTagByTaskId(taskId);
            if(CollectionUtils.isNotEmpty(tags)){
                tags.stream().forEach(map -> {
                    Map tag = taskTagMapper.selectTaskTagById(MapUtils.getString(map,"tag_id"));
                    map.put("tag",tag);
                    result.add(map);
                });
            }

        }
        return result;
    }

	protected Integer getCanReadAttr(String taskId, String memberId, Integer privated) {

		Integer canRead = 1;
		if (null != privated) {
			if (privated > 0) {
				Map canReadMap = baseMapper.selectCanRead(taskId, memberId);
				if (MapUtils.isEmpty(canReadMap)) {
					canRead = 0;
				}
			}
		}
		return canRead;
	}
	
	public Map selectChildCount0(String pid){
        return baseMapper.selectChildCount0(pid);
    }
    public Map selectChildCount1(String pid){
        return baseMapper.selectChildCount1(pid);
    }
	
	 /**
     * 子任务数
     */
	protected List getChildCountAttr(String taskId){
		List childTasks = new ArrayList();
		Map childCount0 = selectChildCount0(taskId);
        Map childCount1 = selectChildCount1(taskId);
        childTasks.add(childCount0.get("tp_count"));
        childTasks.add(childCount1.get("tp_count"));
        return childTasks;
    }

	@Override
	public Result<?> taskDone(Map<String, Object> mmap) {
		String taskId = MapUtils.getString(mmap,"taskId");
        if(StringUtils.isEmpty(taskId)){
            return Result.error("请选择任务");
        }
        SysUser loginUser = iEstarThirdService.getLoginUser();
		String memberId = loginUser.getUsername();
        Integer done =  MapUtils.getInteger(mmap,"done");
        Map taskMap = taskMapper.selectTaskById(taskId);
        taskMap = buildTaskMap(taskMap,memberId);
                if(MapUtils.isEmpty(taskMap)){
                	return Result.error("任务已失效");
        }
        if(MapUtils.getInteger(taskMap,"deleted",0)>0){
        	return Result.error("任务在回收站中无法进行编辑");
        }
        if(StringUtils.isNotEmpty(MapUtils.getString(taskMap,"pId")) && MapUtils.getInteger(taskMap,"parentDone",0)>0){
        	return Result.error("父任务已完成，无法重做子任务");
        }
        if(MapUtils.getInteger(taskMap,"hasUnDone",0)>0){
        	return Result.error("子任务尚未全部完成，无法完成父任务");
        }
        //taskService.lambdaUpdate().eq(TwTask::getId,taskId).set(TwTask::getDone,done).update();
        TwTask twtask = taskMapper.selectById(taskId);
        twtask.setDone(done);
        taskMapper.updateById(twtask);
        String projectId = MapUtils.getString(taskMap, "project_id");
        String stageId = MapUtils.getString(taskMap, "stage_id");
        if (done == 1) {
        	taskWorkflowService.queryRule(projectId, stageId, taskId, null, 1);
        }        
        LambdaQueryWrapper<TwProject> projectQW = new LambdaQueryWrapper<>();
        projectQW.eq(TwProject::getId, MapUtils.getString(taskMap,"project_id"));
        //TwProject project = projectService.getBaseMapper().selectOne(projectQW);
        TwProject project = projectMapper.selectOne(projectQW);
        if(null != project && project.getAutoUpdateSchedule()>0){
            Integer taskCount = baseMapper.selectCountByProjectId(MapUtils.getString(taskMap,"project_id"));
            if(taskCount>0){
                Integer doneTaskCount = baseMapper.selectCountByProjectIdAndDone(MapUtils.getString(taskMap,"project_id"));
                taskCount = taskCount==0?1:taskCount;
                double f1 = new BigDecimal((float)((float)doneTaskCount/(float)taskCount)*100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                project.setSchedule(f1);
                //projectService.updateById(project);
                projectMapper.updateById(project);
            }
        }
        taskHook(memberId,taskId,done>0?"done":"redo","",0,
                "","","",null,null);
        if(StringUtils.isNotEmpty(MapUtils.getString(taskMap,"pId"))){
            taskHook(memberId,MapUtils.getString(taskMap,"pId"),done>0?"doneChild":"redoChild","",0,
                    "","","",null,null);
        }
        return Result.OK("任务完成");
	}
	
	@Override
	public void taskHook(String memberId, String taskId, String type, String toMemberId, Integer isComment,
			String remark, String content, String fileId, Object data, String tag) {
		run(new HashMap() {
			{
				put("memberId", memberId);
				put("taskId", taskId);
				put("toMemberId", toMemberId);
				put("isComment", isComment);
				put("remark", remark);
				put("content", content);
				put("fileId", fileId);
				put("type", type);
				put("is_comment", 0);
				put("data", data);
			}
		});
	}
	 public void run(Map data){
	        int isRobot = MapUtils.getObject(data,"data")!=null && MapUtils.getString((Map)data.get("data"),"is_robot")!=null?1:0;
	        TwProjectLog logData = new  TwProjectLog();
	        logData.setMemberId(MapUtils.getString(data,"memberId"));
	        logData.setSourceId(MapUtils.getString(data,"taskId"));
	        logData.setRemark(MapUtils.getString(data,"remark"));
	        logData.setOpeType(MapUtils.getString(data,"opeType"));
	        logData.setContent(MapUtils.getString(data,"content"));
	        logData.setIsComment(MapUtils.getInteger(data,"isComment"));
	        logData.setToMemberId(MapUtils.getString(data,"toMemberId"));
	        logData.setActionType("task");
	        logData.setIsRobot(isRobot);   
	        //TwTask task = taskService.getTaskById(MapUtils.getString(data,"taskId"));
	        TwTask task = taskMapper.selTaskById(MapUtils.getString(data,"taskId"));
	        logData.setProjectId(task.getProjectId());
	        SysUser toMember = null;
	        if(StringUtils.isNotEmpty(MapUtils.getString(data,"toMemberId"))){
	            toMember =  iEstarThirdService.getUserByUsername(MapUtils.getString(data,"toMemberId"));
	        }
	        String remark="";
	        String content="";
	        String icon = "";
	        switch (MapUtils.getString(data,"type","")){
	            case "create":
	                icon = "plus";
	                remark = "创建了任务 ";
	                content = task.getName();
	                break;
	            case "name":
	                icon = "edit";
	                remark = "更新了内容 ";
	                content = task.getName();
	                break;
	            case "move":
	                icon = "drag";
	                remark = "将任务移动到 "+MapUtils.getString((Map)data.get("data"),"stageName");
	                content = task.getName();
	                break;
	            case "content":
	                icon = "file-text";
	                remark = "更新了备注 ";
	                content = task.getDescription();
	                break;
	            case "clearContent":
	                icon = "file-text";
	                remark = "清空了备注 ";
	                break;
	            case "done":
	                icon = "check";
	                remark = "完成了任务 ";
	                if (StringUtils.isNotEmpty(task.getVersionId()) && !"0".equals(task.getVersionId())) {
	                    projectVersionService.updateSchedule(task.getVersionId());
	                }
	                break;
	            case "redo":
	                icon = "border";
	                remark = "重做了任务 ";
	                if (StringUtils.isNotEmpty(task.getVersionId()) && !"0".equals(task.getVersionId())) {
	                    projectVersionService.updateSchedule(task.getVersionId());
	                }
	                break;
	            case "createChild":
	                icon = "bars";
	                remark = "添加了子任务 "+MapUtils.getString((Map)data.get("data"),"taskName");
	                break;
	            case "doneChild":
	                icon = "bars";
	                remark = "完成了子任务 "+ task.getName();
	                break;
	            case "redoChild":
	                icon = "undo";
	                remark = "重做了子任务 "+ task.getName();
	                break;
	            case "claim":
	                icon = "user";
	                remark = "认领了任务 ";
	                break;
	            case "assign":
	                icon = "user";
	                remark = "指派给了 "+toMember.getRealname();
	                break;
	            case "pri":
	                icon = "user";
	                remark = "更新任务优先级为 "+getPriTextAttr(String.valueOf(task.getPri()));
	                break;
	            case "status":
	                icon = "deployment-unit";
	                remark = "修改执行状态为 " +getStatusTextAttr(String.valueOf(task.getStatus())) ;
	                break;
	            case "removeExecutor":
	                icon = "user-delete";
	                remark = "移除了执行者 ";
	                break;
	            case "changeState":
	                icon = "edit";
	                TwTaskStages taskStage = getTaskStageById(task.getStageId());
	                remark = "将任务移动到 "+taskStage.getName();
	                break;
	            case "inviteMember":
	                icon = "user-add";
	                remark = "添加了参与者 "+toMember.getRealname();
	                break;
	            case "removeMember":
	                icon = "user-delete";
	                remark = "移除了参与者 "+toMember.getRealname();
	                break;
	            case "setBeginTime":
	                icon = "calendar";
	                remark = "更新开始时间为 "+task.getBeginTime();
	                break;
	            case "clearBeginTime":
	                icon = "calendar";
	                remark = "清除了开始时间 ";
	                break;
	            case "setEndTime":
	                icon = "calendar";
	                remark = "更新截止时间为 "+ task.getEndTime();
	                break;
	            case "clearEndTime":
	                icon = "calendar";
	                remark = "清除了截止时间 ";
	                break;
	            case "recycle":
	                icon = "delete";
	                remark = "把任务移到了回收站 ";
	                break;
	            case "recovery":
	                icon = "undo";
	                remark = "恢复了任务 ";
	                break;
	            case "setWorkTime":
	                icon = "clock-circle";
	                remark = "更新预估工时为 "+task.getWorkTime();
	                break;
	            case "linkFile":
	                icon = "link";
	                remark = "关联了文件 ";
	                content = "<a target='_blank' class='muted' href='"+MapUtils.getString((Map)data.get("data"),"url")+ "'>{$data['data']['title']}</a>";

	                break;
	            case "unlinkFile":
	                icon = "disconnect";
	                remark = "取消关联文件";
	                content = "<a target='_blank' class='muted' href='"+MapUtils.getString((Map)data.get("data"),"url")+ "'>"+MapUtils.getString((Map)data.get("data"),"title")+ "</a>";
	                break;
	            case "comment":
	                icon = "file-text";
	                remark = MapUtils.getString(data,"content","");
	                content = MapUtils.getString(data,"content","");
	                break;
	            default:
	                icon = "plus";
	                remark = " 创建了任务 ";
	                break;
	        }
	        logData.setIcon(icon);
	        if(logData.getIsRobot()>0){
	            logData.setIcon("alert");
	        }
	        if(StringUtils.isNotEmpty(MapUtils.getString(data,"remark"))){
	            logData.setRemark(MapUtils.getString(data,"remark"));
	        }else{
	            logData.setRemark(remark);
	        }
	        if(StringUtils.isNotEmpty(MapUtils.getString(data,"content"))){
	            logData.setContent(MapUtils.getString(data,"content"));
	        }else{
	            logData.setContent(content);
	        }
	        projectLogService.save(logData);

	        //工作流事件
	        //触发推送的事件
	        ArrayList<String> notifyActions = new ArrayList<String>(){{
	            add("done");add("redo");add("assign");add("comment");
	        }};
	        SysUser member = iEstarThirdService.getUserByUsername(MapUtils.getString(data,"memberId"));
	        
	        List<String> taskMembers = new ArrayList<>();
	        if(notifyActions.contains(MapUtils.getString(data,"type","NULL"))){
	            if(MapUtils.getString(data,"type","NULL").equals("comment")){
	                //notifyData.setType("notice");
	                if(ObjectUtil.isNotEmpty(MapUtils.getObject(data,"data"))){
	                    List<String> stringList = (List)MapUtils.getObject((Map)MapUtils.getObject(data,"data"),"list",new ArrayList());
	                    for(String item:stringList){
	                        item = item.split(" ")[0];
	                        String memberId = baseMapper.selectMemberIdOne(task.getId(),item);
	                        if(StringUtils.isNotEmpty(memberId)){
	                            taskMembers.add(memberId);
	                        }
	                    }
	                }
	            }else{
	                //List<TwTaskMember> taskMemberList = taskMemberService.lambdaQuery().eq(TwTaskMember::getTaskId,task.getId()).list();
	            	LambdaQueryWrapper<TwTaskMember> taskMemberQW = new LambdaQueryWrapper<>();
	            	taskMemberQW.eq(TwTaskMember::getTaskId,task.getId());
	            	List<TwTaskMember> taskMemberList = taskMemberMapper.selectList(taskMemberQW);
	            	if(CollectionUtils.isNotEmpty(taskMemberList)){
	                    for(TwTaskMember taskMember:taskMemberList){
	                        taskMembers.add(taskMember.getMemberId());
	                    }
	                }
	            }
	            if(CollectionUtils.isNotEmpty(taskMembers)){
	                for(String taskMemberId:taskMembers){
	                    if(taskMemberId.equals(MapUtils.getString(data,"memberId"))){
	                        continue;//跳过产生者
	                    }
	                    iFlowThirdService.sendSysAnnouncement(MapUtils.getString(data,"memberId"), taskMemberId, member.getRealname()+": "+remark, task.getName(), "1");//setMsgCategory=1是通知
	                }
	            }
	        }

	    }

	@Override
	public TwTaskStages getTaskStageById(String id) {
		LambdaQueryWrapper<TwTaskStages> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TwTaskStages::getId, id);
        return baseMapper.selectOne(queryWrapper);
	}

	@Override
	public Result<?> sortTask(Map<String, Object> mmap) {
		String stageId = MapUtils.getString(mmap,"stageId");
        String ids = MapUtils.getString(mmap,"ids");
        if(StringUtils.isEmpty(ids)){
            return Result.error("参数有误！");
        }
        SysUser loginUser = iEstarThirdService.getLoginUser();
		String userId = loginUser.getUsername();
        List<String> listids = Arrays.asList(ids.split(","));
        //TwTaskStages taskStage= taskStagesService.lambdaQuery().eq(TwTaskStages::getId,stageId).one();
        LambdaQueryWrapper<TwTaskStages> taskStagesQW = new LambdaQueryWrapper<>();
        taskStagesQW.eq(TwTaskStages::getId,stageId);
        TwTaskStages taskStage=  this.baseMapper.selectOne(taskStagesQW);
        for(int i=0;i<listids.size();i++){
            //TwTask task = taskService.lambdaQuery().eq(TwTask::getId,listids.get(i)).one();
        	//taskService.lambdaUpdate().set(TwTask::getSort,i).set(TwTask::getStageId,stageId).eq(TwTask::getId,listids.get(i)).update();
            LambdaQueryWrapper<TwTask> taskQW = new LambdaQueryWrapper<>();
            taskQW.eq(TwTask::getId,listids.get(i));
            TwTask task =  taskMapper.selectOne(taskQW);
            task.setStageId(stageId);
            task.setSort(i);
            taskMapper.update(task, taskQW);
            if(!stageId.equals(task.getStageId())){
                taskHook(userId,listids.get(i),"move","",0,
                        "","","",new HashMap(){{
                            put("stageName",taskStage.getName());
                        }},null);
            }
        }
		return null;
	}

	@Override
	public Result<?> getTaskStages(Map<String, Object> mmap) {
		String projectId = MapUtils.getString(mmap,"projectId");
        if(null == projectId){
            return Result.error("请选择一个项目");
        }
        IPage<TwTaskStages> ipage = Constant.createPage(mmap);
        Map params = new HashMap();
        params.put("projectId",projectId);
        ipage = selectTaskStageByProjectId(ipage,params);

        if(null == ipage){
            ipage = new Page<>();
        }
        Map data = Constant.createPageResultMap(ipage);
        return Result.OK(data);
	}

	private IPage<TwTaskStages> selectTaskStageByProjectId(IPage<TwTaskStages> ipage, Map params) {
		return baseMapper.selectTaskStageByProjectIdForPage(ipage, params);
	}

	
}
