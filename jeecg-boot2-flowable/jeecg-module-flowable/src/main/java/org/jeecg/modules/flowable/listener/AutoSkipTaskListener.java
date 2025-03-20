package org.jeecg.modules.flowable.listener;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;

import org.flowable.task.service.delegate.DelegateTask;
import org.jeecg.common.util.SpringContextUtils;

import org.jeecg.modules.flowable.common.enums.FlowComment;

/**
 * 自动跳过流程任务监听
 * @author nbacheng
 * @date 2023-02-18
*/

public class AutoSkipTaskListener implements TaskListener{

	private TaskService taskService = SpringContextUtils.getBean(TaskService.class);
	
    @Override
    public void notify(DelegateTask delegateTask) {
    	String processInstanceId = delegateTask.getProcessInstanceId();
        String taskId = delegateTask.getId();
        // 流程实例为空则结束
        if(StringUtils.isBlank(processInstanceId) && StringUtils.isBlank(taskId)){
            return;
        }
        if(StringUtils.isBlank(delegateTask.getAssignee())){
          // 添加处理意见
          taskService.addComment(taskId, processInstanceId,FlowComment.SKIP.getType(), FlowComment.SKIP.getRemark() + ":审批人为空字段跳过");
          // 自动审批通过
          taskService.complete(taskId);
        }
        
    }

}
