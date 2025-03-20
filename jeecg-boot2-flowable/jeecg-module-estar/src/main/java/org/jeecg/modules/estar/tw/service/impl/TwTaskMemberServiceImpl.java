package org.jeecg.modules.estar.tw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectMember;
import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.entity.TwTaskMember;
import org.jeecg.modules.estar.tw.mapper.TwProjectCollectMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMemberMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectMemberService;
import org.jeecg.modules.estar.tw.service.ITwProjectService;
import org.jeecg.modules.estar.tw.service.ITwTaskMemberService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowService;
import org.jeecg.modules.estar.tw.util.StringUtils;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


/**
 * @Description: 项目任务团队表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwTaskMemberServiceImpl extends ServiceImpl<TwTaskMemberMapper, TwTaskMember> implements ITwTaskMemberService {

	//@Autowired
	//ITwProjectService projectService;
	@Autowired
	TwProjectMapper projectMapper;
	//@Autowired
	//ITwTaskService taskService;
	@Autowired
	TwTaskMapper taskMapper; 
	@Autowired
	ITwTaskStagesService taskStagesService;
	@Autowired
	ITwProjectMemberService projectMemberService;
	@Autowired
	TwProjectCollectMapper projectCollectMapper;
	@Autowired
	ITwTaskWorkflowService taskWorkflowService;
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result<?> inviteMember(String memberId, String taskId, Integer isExecutor, Integer isOwner,
			boolean fromCreate, boolean isRobot) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		memberId = StringUtils.isEmpty(memberId)?"":memberId;
		//TwTask task = taskService.lambdaQuery().eq(TwTask::getId,taskId).eq(TwTask::getDeleted,0).one();
		LambdaQueryWrapper<TwTask> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(TwTask::getId,taskId).eq(TwTask::getDeleted,0);
		TwTask task = taskMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(task)){
        	return Result.error("任务已失效！");
        }
        TwTaskMember taskExecutor = lambdaQuery().eq(TwTaskMember::getIsExecutor,1).eq(TwTaskMember::getTaskId,taskId).one();
        if(null != taskExecutor && taskExecutor.getMemberId().equals(memberId)){
            return Result.OK(new TwTaskMember());
        }
        if(isExecutor>0){
            lambdaUpdate().set(TwTaskMember::getIsExecutor,0).eq(TwTaskMember::getTaskId,taskId).update();
        }
        if(StringUtils.isNotEmpty(memberId)){
        	TwTaskMember hasJoined = lambdaQuery().eq(TwTaskMember::getMemberId,memberId).eq(TwTaskMember::getTaskId,taskId).one();
            if(!ObjectUtils.isEmpty(hasJoined)){
                //taskService.lambdaUpdate().set(TwTask::getAssignTo,memberId).eq(TwTask::getId,taskId).update();
            	TwTask twtask = taskMapper.selectById(taskId);
            	twtask.setAssignTo(memberId);
            	taskMapper.updateById(twtask);
            	taskWorkflowService.queryRule(task.getProjectId(), task.getStageId(), task.getId(), memberId, 3);
                
                lambdaUpdate().set(TwTaskMember::getIsExecutor,1).eq(TwTaskMember::getTaskId,taskId).eq(TwTaskMember::getMemberId,memberId).update();
                String logType ="assign";
                if(userId.equals(memberId)){
                    logType="claim";
                }
                taskStagesService.taskHook(userId,taskId,logType,memberId,0,
                        "","","",new HashMap(){{
                            put("is_robot",isRobot);
                        }},null);
                return Result.OK(new TwTaskMember());
            }
        }
        if(StringUtils.isEmpty(memberId)){
            //taskService.lambdaUpdate().set(TwTask::getAssignTo,memberId).eq(TwTask::getId,taskId).update();
            TwTask twtask = taskMapper.selectById(taskId);
            twtask.setAssignTo(memberId);
            taskMapper.updateById(twtask);
        	
            if(!fromCreate){
                if(ObjectUtil.isNotEmpty(taskExecutor)){
                	taskStagesService.taskHook(userId,taskId,"removeExecutor",taskExecutor.getMemberId(),0,
                            "","","",new HashMap(){{
                                put("is_robot",isRobot);
                            }},null);
                }
            }
            return Result.OK(new TwTaskMember());
        }
        TwTaskMember taskMember = new TwTaskMember();
        taskMember.setMemberId(memberId);
        taskMember.setTaskId(taskId);
        taskMember.setIsExecutor(isExecutor);
        taskMember.setIsOwner(isOwner);
        taskMember.setJoinTime(new Date());
        save(taskMember);
        if(isExecutor>0){
            //taskService.lambdaUpdate().eq(TwTask::getId,taskId).set(TwTask::getAssignTo,memberId).update();
        	TwTask twtask = taskMapper.selectById(taskId);
            twtask.setAssignTo(memberId);
            taskMapper.updateById(twtask);
        	if(userId.equals(memberId)){
            	taskStagesService.taskHook(userId,taskId,"claim","",0,
                        "","","",new HashMap(){{
                            put("is_robot",isRobot);
                        }},null);
            }else{
            	taskStagesService.taskHook(userId,taskId,"claim",memberId,0,
                        "","","",new HashMap(){{
                            put("is_robot",isRobot);
                        }},null);
            }
        }
        if(StringUtils.isNotEmpty(memberId)){
        	//TwProject project = projectService.lambdaQuery().eq(TwProject::getId,task.getProjectId()).one();
        	TwProject project = projectMapper.selectById(task.getProjectId());
        	projectMemberService.inviteMember(memberId,project==null?"":project.getId(),0);
        }
        return Result.OK(taskMember);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IPage<Map> getTaskMemberByTaskId(IPage iPage, String taskId) {
		return baseMapper.selectTaskMemberByTaskId(iPage, taskId);
	}

	@Override
	public Result<?> inviteMemberBatch(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		String memberIds = MapUtils.getString(mmap,"memberIds");
        String taskId = MapUtils.getString(mmap,"taskId");
        if(StringUtils.isEmpty(memberIds) || StringUtils.isEmpty(taskId)){
            return Result.error("数据异常！");
        }
        
        //TwTask task = taskService.getTaskById(taskId);
        TwTask task = taskMapper.selTaskById(taskId);
        if(ObjectUtils.isEmpty(task)){
        	return Result.error("该任务已失效！");
        }
        boolean isAll = false;
        JSONArray memberCodeArray = JSON.parseArray(memberIds);
        //List<>
        List<String> memberCodesList = new ArrayList<>();
        if(memberIds.indexOf("all") != -1){
            isAll = true;
            List<TwProjectMember> list= projectMemberService.lambdaQuery().eq(TwProjectMember::getProjectId,task.getProjectId()).list();
            if(CollectionUtil.isNotEmpty(list)){
                list.forEach(projectMember -> {
                    memberCodesList.add(projectMember.getUserId());
                });
            }
        }else{
            if(StringUtils.isNotEmpty(memberCodeArray)) {
                for (Object obj : memberCodeArray) {
                    if(ObjectUtil.isNotEmpty(obj)){
                        memberCodesList.add(String.valueOf(obj));
                    }
                }
            }
        }
        TwTaskMember taskMember = lambdaQuery().eq(TwTaskMember::getIsOwner,1)
                .eq(TwTaskMember::getTaskId,taskId).one();
        boolean finalIsAll = isAll;
        memberCodesList.forEach(memberId ->{
            if(!memberId.equals(taskMember.getMemberId())){
            	TwTaskMember hasJoined = lambdaQuery().eq(TwTaskMember::getMemberId,memberId)
                        .eq(TwTaskMember::getTaskId,taskId).one();
                if(ObjectUtil.isNotEmpty(hasJoined)){
                    if(!finalIsAll){
                        if(hasJoined.getIsExecutor()>0){
                            //taskService.lambdaUpdate().eq(TwTask::getId,taskId).set(TwTask::getAssignTo,"").update();
                        	TwTask twtask = taskMapper.selectById(taskId);
                            twtask.setAssignTo("");
                            taskMapper.updateById(twtask);
                            taskStagesService.taskHook(userId,taskId,"removeExecutor",memberId,0,
                                    "","","",null,null);
                        }
                        lambdaUpdate().eq(TwTaskMember::getTaskId,taskId).eq(TwTaskMember::getMemberId,memberId).remove();
                        taskStagesService.taskHook(userId,taskId,"removeMember",memberId,0,
                                "","","",null,null);
                    }
                }else{
                	TwTaskMember saveTaskMember = new TwTaskMember();
                	saveTaskMember.setMemberId(memberId);
                	saveTaskMember.setTaskId(taskId);
                	saveTaskMember.setIsExecutor(0);
                	saveTaskMember.setJoinTime(new Date());
                    save(saveTaskMember);
                    taskStagesService.taskHook(userId,taskId,"inviteMember",memberId,0,
                            "","","",null,null);
                }
            }
        });
		return Result.OK("批量要求成员成功");
	}

}
