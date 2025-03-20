package org.jeecg.modules.flowable.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;
import org.jeecg.modules.flowable.utils.flowExp;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 多实例collect用户处理类
 *
 * @author nbacheng
 * @date 2022-10-16
 */
@AllArgsConstructor
@Component("multiInstanceHandler")
public class MultiInstanceHandler {
	RuntimeService runtimeService = SpringContextUtils.getBean(RuntimeService.class);

	@SuppressWarnings("unchecked")
	public Set<String> getUserName(DelegateExecution execution) {
        Set<String> candidateUserName = new LinkedHashSet<>();
        FlowElement flowElement = execution.getCurrentFlowElement();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
         // 获取流程发起人
    		ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(execution.getProcessInstanceId())
                    .singleResult();
            String startUserId = processInstance.getStartUserId();
            if (CollUtil.isNotEmpty(userTask.getCandidateUsers())) {
            	List<String> groups = userTask.getCandidateUsers();
            	if (groups.size()!= 0 && StringUtils.contains(groups.get(0), "${flowExp.getDynamic")) {
            		candidateUserName.addAll(getDynamicUsers(groups,startUserId));
            		
				}
            	else if (groups.size()!= 0 && (StringUtils.contains(groups.get(0), "DepManagerHandler") || StringUtils.contains(groups.get(0), "DeptHeadList")) ) {
            		candidateUserName.addAll(getInitiatorDepManagers(startUserId));
				}
            	else {
            		candidateUserName.addAll(userTask.getCandidateUsers());
            	}
            	
            } else if (CollUtil.isNotEmpty(userTask.getCandidateGroups())) {
            	List<String> groups = userTask.getCandidateGroups();
            	if (groups.size()!= 0 && StringUtils.contains(groups.get(0), "${flowExp.getDynamic")) {
            		candidateUserName.addAll(getDynamicUsers(groups,startUserId));	
				}
            	else {
	                IFlowThirdService iFlowThirdService = SpringContextUtils.getBean(IFlowThirdService.class);
	                groups.forEach(item -> {
	                     List<SysUser> listuserName = iFlowThirdService.getUsersByRoleId(item);
	                     for(SysUser sysuser : listuserName) {
	                        candidateUserName.add(sysuser.getUsername());
	                     }
	                });
            	} 
            }
        }
        return candidateUserName;
    }
	
	@SuppressWarnings("unchecked")
    private List<String> getDynamicUsers(List<String> groups,String startUserId) {
    	String methodname = StringUtils.substringBetween(groups.get(0), ".", "(");
		List<String> list = new ArrayList<String>();
		flowExp flowexp = SpringContextUtils.getBean(flowExp.class);
		Object[] argsPara=new Object[]{};
		try {
			list = (List<String>) flowexp.invokeMethod(flowexp, methodname,argsPara);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
    }
	
	private List<String> getInitiatorDepManagers(String startUserId) {
		List<String> list = new ArrayList<String>();
		flowExp flowexp = SpringContextUtils.getBean(flowExp.class);
		try {
			list = flowexp.getInitiatorDepManagers(startUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
    }
}
