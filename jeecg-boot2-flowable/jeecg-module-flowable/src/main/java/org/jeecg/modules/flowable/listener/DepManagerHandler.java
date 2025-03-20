package org.jeecg.modules.flowable.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;

import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;

/**
 * 部门经理处理类
 *
 * @author nbacheng
 * @date 2023-08-06
 */
@AllArgsConstructor
@Component("DepManagerHandler")
public class DepManagerHandler {

	private IFlowThirdService flowThirdService = SpringContextUtils.getBean(IFlowThirdService.class);
	RuntimeService runtimeService = SpringContextUtils.getBean(RuntimeService.class);

	public List<String> getUsers(DelegateExecution execution) {
		List<String> assignUserName = new ArrayList<String>();
		FlowElement flowElement = execution.getCurrentFlowElement();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            if ( ObjectUtil.isNotEmpty(userTask.getCandidateGroups())) {
            	if(StringUtils.contains(userTask.getCandidateGroups().get(0),"DepManagerHandler")) {
            		 // 获取流程发起人
            		ProcessInstance processInstance = runtimeService
                            .createProcessInstanceQuery()
                            .processInstanceId(execution.getProcessInstanceId())
                            .singleResult();
                    String startUserId = processInstance.getStartUserId();
                    // 获取部门负责人列表
            		List<String> depIds = flowThirdService.getDepartIdsByUsername(startUserId);
            		List<String> DepHeadlist = new ArrayList<String>();
            		for(String depId: depIds) {
            			List<String> depList = flowThirdService.getDeptHeadByDepId(depId);
            			if(depList != null) {
            				DepHeadlist.addAll(depList);
            			}
            			
            		}
            		// 部门负责人列表去重
            		if(!DepHeadlist.isEmpty() ) {
            		    for (String str : DepHeadlist) {
            		        if (!assignUserName.contains(str)) {
            		        	assignUserName.add(str);
            		        }
            		    }
            		}
            	}
            }
        }    
        return assignUserName;
		
	}
}
