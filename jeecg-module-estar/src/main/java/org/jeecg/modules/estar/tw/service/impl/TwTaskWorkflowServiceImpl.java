package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwTask;
import org.jeecg.modules.estar.tw.entity.TwTaskMember;
import org.jeecg.modules.estar.tw.entity.TwTaskWorkflow;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMemberMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskWorkflowMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskMemberService;
import org.jeecg.modules.estar.tw.service.ITwTaskService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowRuleService;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowService;
import org.jeecg.modules.estar.tw.util.Constant;
import org.jeecg.modules.estar.tw.util.RedisCache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.tw.entity.TwTaskWorkflowRule;


/**
 * @Description: 任务工作流表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */

@Slf4j
@Service
public class TwTaskWorkflowServiceImpl extends ServiceImpl<TwTaskWorkflowMapper, TwTaskWorkflow> implements ITwTaskWorkflowService {

	@Autowired
	TwTaskWorkflowMapper taskWorkflowMapper;
	@Autowired
	ITwTaskWorkflowRuleService taskWorkflowRuleService;
	//@Autowired
	//ITwTaskMemberService taskMemberService;
	@Autowired
	TwTaskMemberMapper taskMemberMapper; 
	//@Autowired
	//ITwTaskService taskService;
	@Autowired
	TwTaskMapper taskMapper;
	@Autowired
    private RedisCache redisCache;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getList(String projectId) {
		return taskWorkflowMapper.getWorkflowByProjectId(projectId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveAndRules(String projectId, String organizationId, String taskWorkflowName,
			String taskWorkflowRules) {
		JSONObject rules = JSONObject.parseObject(taskWorkflowRules);
		TwTaskWorkflow taskWorkflow = new TwTaskWorkflow();
		taskWorkflow.setName(taskWorkflowName);
		taskWorkflow.setOrganizationId(organizationId);
		taskWorkflow.setProjectId(projectId);
		boolean save = save(taskWorkflow);
		boolean saveRules = saveRules(taskWorkflow.getId(), rules);
		if (save && saveRules) {
            return true;
        } else {
        	return false;
        }
	}

	private boolean saveRules(String id, JSONObject rules) {
		String firstObj = (String) rules.get("firstObj");
        if (StrUtil.isNotEmpty(firstObj)) {
            List<TwTaskWorkflowRule> list = new ArrayList<>();
            TwTaskWorkflowRule rule01 = new TwTaskWorkflowRule();
            rule01.setSort(1);
            rule01.setType(0);
            rule01.setAction(0);
            rule01.setObjectId(firstObj);
            rule01.setWorkflowId(id);
            list.add(rule01);
            JSONObject object02 = rules.getJSONObject("firstAction");
            buildRule(list, object02, id, 2, 0);
            JSONObject object03 = rules.getJSONObject("firstResult");
            buildRule(list, object03, id, 3, 0);
            JSONObject object04 = rules.getJSONObject("lastResult");
            boolean rule04 = buildRule(list, object04, id, 4, 0);
            JSONObject object05 = rules.getJSONObject("state");
            if (rule04) {
                buildRule(list, object05, id, 5, 1);
            } else {
                buildRule(list, object05, id, 4, 1);
            }
            boolean saveBatch = taskWorkflowRuleService.saveBatch(list);
            if (!saveBatch) {
            	return false;
            }
            return true;
        } else {
        	return false;
        }
	}
	
	private boolean buildRule(List<TwTaskWorkflowRule> list, JSONObject object, String id, int sort, int flag) {
        Integer action = (Integer) object.get("action");
        TwTaskWorkflowRule build = new TwTaskWorkflowRule();
        if (flag == 1) {
            Integer val = (Integer) object.get("value");
            build.setType(3);
            build.setAction(val);
            build.setObjectId(null);
            build.setSort(sort);
            build.setWorkflowId(id);
        } else {
            if (action == 3) {
            	build.setType(1);
            } else {
            	build.setType(0);
            }
            String val = (String) object.get("value");
            if (sort == 4) {
                if (StrUtil.isEmpty(val)) {
                    return false;
                }
            }
            build.setAction(action);
            build.setObjectId(val);
            build.setSort(sort);
            build.setWorkflowId(id);
        }
        return list.add(build);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean editAndRules(String id, String taskWorkflowName,
			String taskWorkflowRules) {
		JSONObject rules = JSONObject.parseObject(taskWorkflowRules);
		boolean update = lambdaUpdate().set(TwTaskWorkflow::getName, taskWorkflowName)
				                       .set(TwTaskWorkflow::getUpdateTime, new Date())
				                       .eq(TwTaskWorkflow::getId, id)
				                       .update();
		boolean remove = taskWorkflowRuleService.remove(Wrappers.<TwTaskWorkflowRule>lambdaQuery().eq(TwTaskWorkflowRule::getWorkflowId, id));
		boolean saveRules = saveRules(id, rules);
		if (update && remove && saveRules) {
            return true;
        } else {
        	return false;
        }
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeAndRules(String id) {
		boolean remove = remove(Wrappers.<TwTaskWorkflow>lambdaQuery().eq(TwTaskWorkflow::getId, id));
        boolean removeRule = taskWorkflowRuleService.remove(Wrappers.<TwTaskWorkflowRule>lambdaQuery().eq(TwTaskWorkflowRule::getWorkflowId, id));
        if (remove && removeRule) {
            return true;
        } else {
        	return false;
        }
	}

	@Override
	public void queryRule(String projectId, String stageId, String taskId, String memberId, Integer action) {
		//此项目的所有规则
        List<TwTaskWorkflow> workflowList = getTaskWorkFlow(projectId);
        //找到符合条件的规则
        List<TwTaskWorkflow> theWorkflow = getTheWokFlow(workflowList, stageId, memberId, action);
        //遍历规则，做出任务修改
        LambdaUpdateWrapper<TwTask> taskWrapper = Wrappers.<TwTask>lambdaUpdate().eq(TwTask::getId, taskId);
        if (theWorkflow != null) {
            theWorkflow.forEach(o -> {
                List<TwTaskWorkflowRule> ruleList = o.getWorkflowRuleList();
                TwTaskWorkflowRule rule03 = ruleList.stream().filter(o1 -> o1.getSort() == 3).findFirst().orElse(null);
                if (rule03 != null && rule03.getAction() == 3) {
                    taskWrapper.set(TwTask::getAssignTo, rule03.getObjectId());
                    if (StrUtil.isNotEmpty(rule03.getObjectId())) {
                    	updateTaskMember(taskId, rule03.getObjectId());
                    }
                } else if (rule03 != null) {
                    taskWrapper.set(TwTask::getStageId, rule03.getObjectId());
                }
                TwTaskWorkflowRule rule04 = ruleList.stream().filter(o1 -> o1.getSort() == 4).findFirst().orElse(null);
                TwTaskWorkflowRule rule05 = ruleList.stream().filter(o1 -> o1.getSort() == 5).findFirst().orElse(null);
                if (rule03 != null && rule04 != null && StrUtil.isNotEmpty(rule04.getObjectId())) {
                    if (rule03.getAction() == 3) {
                        taskWrapper.set(TwTask::getStageId, rule04.getObjectId());
                    }
                    if (rule03.getAction() == 0) {
                        taskWrapper.set(TwTask::getAssignTo, rule04.getObjectId());
                        if (StrUtil.isNotEmpty(rule04.getObjectId())) {
                        	updateTaskMember(taskId, rule04.getObjectId());
                        }
                    }
                } else if (rule04 != null && StrUtil.isEmpty(rule04.getObjectId())) {
                    rule05 = rule04;
                }
                if (rule05 != null) {
                    if (rule05.getAction() == 1) {
                        taskWrapper.set(TwTask::getDone, 1);
                    }
                    if (rule05.getAction() == 2) {
                        taskWrapper.set(TwTask::getDone, 0);
                    }
                }
                //boolean update = taskService.update(taskWrapper);
                int update = taskMapper.update(null, taskWrapper);
                log.info("根据流转规则修改任务{}", update);
            });
        }
		
	}
	
	private void updateTaskMember(String taskId, String memberId) {
		//TwTaskMember one = taskMemberService.lambdaQuery().eq(TwTaskMember::getTaskId, taskId).eq(TwTaskMember::getMemberId, memberId).one();
		//boolean update = taskMemberService.lambdaUpdate().set(TwTaskMember::getIsExecutor, 0).eq(TwTaskMember::getTaskId, taskId).update();
		LambdaQueryWrapper<TwTaskMember> taskMemberQW = new LambdaQueryWrapper<>();
		taskMemberQW.eq(TwTaskMember::getTaskId, taskId).eq(TwTaskMember::getMemberId, memberId);
		TwTaskMember one = taskMemberMapper.selectOne(taskMemberQW);
		one.setIsExecutor(0);		
		LambdaQueryWrapper<TwTaskMember> taskMemberQW2 = new LambdaQueryWrapper<>();
		taskMemberQW2.eq(TwTaskMember::getTaskId, taskId);
		int update = taskMemberMapper.update(one, taskMemberQW2);
		//boolean update = taskMemberService.lambdaUpdate().set(TwTaskMember::getIsExecutor, 0).eq(TwTaskMember::getTaskId, taskId).update();
        if (one == null) {
        	TwTaskMember build = new TwTaskMember();
        	build.setTaskId(taskId);
        	build.setMemberId(memberId);
        	build.setIsExecutor(1);
        	build.setJoinTime(new Date());
            //boolean save = taskMemberService.save(build);
        	int save = taskMemberMapper.insert(build);
            log.info("任务成员修改{}，任务成功新增{}", update, save);
       } else {
            //boolean update1 = taskMemberService.lambdaUpdate().set(TwTaskMember::getIsExecutor, 1).eq(TwTaskMember::getTaskId, taskId).eq(TwTaskMember::getMemberId, memberId).update();
    	    LambdaQueryWrapper<TwTaskMember> taskMemberUpdate = new LambdaQueryWrapper<>();
    	    taskMemberUpdate.eq(TwTaskMember::getTaskId, taskId).eq(TwTaskMember::getMemberId, memberId);
    	    TwTaskMember updateone = taskMemberMapper.selectOne(taskMemberUpdate);
    	    updateone.setIsExecutor(1);
    	    int update1 = taskMemberMapper.update(updateone, taskMemberUpdate);
    	    log.info("任务成员修改{}，成员修改{}", update, update1);
        }
   }
	
	private List<TwTaskWorkflow> getTheWokFlow(List<TwTaskWorkflow> workflowList, String stageCode, String memberCode, Integer action) {
        List<String> flowCode = workflowList.stream().filter(o -> {
            List<TwTaskWorkflowRule> workflowRuleList = o.getWorkflowRuleList();
            boolean flag = false;
            for (TwTaskWorkflowRule rule : workflowRuleList) {
                if (rule.getSort() == 1) {
                    if (StrUtil.equals(rule.getObjectId(), stageCode)) {
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (rule.getSort() == 2) {
                    if (action.equals(rule.getAction())) {
                        if (action == 3) {
                            if (StrUtil.equals(memberCode, rule.getObjectId())) {
                                flag = true;
                            } else {
                                flag = false;
                                break;
                            }
                        } else {
                            flag = true;
                        }
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
            return flag;
        }).map(TwTaskWorkflow::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(flowCode)) {
            return workflowList.parallelStream().filter(o -> flowCode.contains(o.getId())).collect(Collectors.toList());
        }
        return null;
    }
	
	private List<TwTaskWorkflow> getTaskWorkFlow(String projectId) {
		List<TwTaskWorkflow> list = redisCache.getCacheObject(Constant.PROJECTRULE + projectId);
        if (CollUtil.isEmpty(list)) {
            list = lambdaQuery().eq(TwTaskWorkflow::getProjectId, projectId).list();
            if (CollUtil.isNotEmpty(list)) {
                List<String> codeList = list.parallelStream().map(TwTaskWorkflow::getId).collect(Collectors.toList());
                List<TwTaskWorkflowRule> rules = taskWorkflowRuleService.lambdaQuery().in(TwTaskWorkflowRule::getWorkflowId, codeList).list();
                if (CollUtil.isNotEmpty(rules)) {
                    Map<String, List<TwTaskWorkflowRule>> collect = rules.stream().collect(Collectors.groupingBy(TwTaskWorkflowRule::getWorkflowId));
                    list.forEach(o -> {
                        o.setWorkflowRuleList(collect.get(o.getId()));
                    });
                }
            }
            redisCache.setCacheObject(Constant.PROJECTRULE + projectId, list, 60, TimeUnit.MINUTES);
        }
        return list;
    }

}
