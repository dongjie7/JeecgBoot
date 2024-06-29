package org.jeecg.modules.flowable.listener;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.jeecg.common.util.SpringContextUtils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 根据表单内容动态设置审批人
 * @author nbacheng
 * @date 2023-3-02
*/

public class SetAssigneeTaskListener implements TaskListener{

	private static final long serialVersionUID = 1L;
	private TaskService taskService = SpringContextUtils.getBean(TaskService.class);
	
	@Override
	public void notify(DelegateTask delegateTask) {
		
		String processInstanceId = delegateTask.getProcessInstanceId();
		String processDefinitionId = delegateTask.getProcessDefinitionId();
		String taskId = delegateTask.getId();
		String newAssignee;
		
		//表单里面的值,相关逻辑可以根据自己需要进行处理		
		String formvalue = delegateTask.getVariable("assigneeId").toString().trim();
		
		if (StringUtils.equals(formvalue, "admin")) {
			newAssignee = "admin";
		}
		else if (StringUtils.equals(formvalue, "zhangsan")) {
			newAssignee = "zhangsan";
		}
		else {
			newAssignee = "jeecg";
		}
		Map<String, Object> map = taskService.getVariables(taskId);
		map.put("SetAssigneeTaskListener", newAssignee);
		taskService.setVariables(taskId, map);
	}
}
