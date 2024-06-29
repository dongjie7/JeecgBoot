package org.jeecg.modules.flowable.listener;

import java.util.Objects;

import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
/**
 * 多实例会签审核结果全局监听器
 * @author nbacheng
 * @date 2022-09-22
*/
@Slf4j
@Component("MutiInstanceExecutionListener")
public class MutiInstanceExecutionListener implements ExecutionListener, ApplicationContextAware {

	private static final long serialVersionUID = 1L;
	private static  ApplicationContext applicationContext;

	
	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		RedisUtil redisUtil = applicationContext.getBean(RedisUtil.class);
		FlowNode flowNode = (FlowNode) execution.getCurrentFlowElement();
		if(Objects.nonNull(flowNode)) {
			if(flowNode instanceof UserTask ){
         	UserTask userTask = (UserTask) flowNode;
         	MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
         	if (Objects.nonNull(multiInstance)) {
         		if (Objects.nonNull(execution.getVariable("nrOfCompletedInstances"))) {
         			int nrOfCompletedInstances = (int) execution.getVariable("nrOfCompletedInstances");
         			int nrOfInstances = (int) execution.getVariable("nrOfInstances");
             		if(multiInstance.isSequential()) {
             			if((nrOfCompletedInstances + 1) >= nrOfInstances){//结束会签流程前一个节点提示进行用户选择
             				
             				redisUtil.set(CommonConstant.MUTIINSTANCE_NEXT_FINISH + execution.getProcessInstanceId(), execution.getProcessInstanceId());
             			}
             			if(nrOfCompletedInstances >= nrOfInstances) {
             				redisUtil.removeAll(CommonConstant.MUTIINSTANCE_NEXT_FINISH + execution.getProcessInstanceId());
             			}
             		}
             		else if(multiInstance.getCompletionCondition().equals("${nrOfCompletedInstances>=nrOfInstances}")) {
             			if((nrOfCompletedInstances + 1) >= nrOfInstances){//结束会签流程前一个节点提示进行用户选择
             				redisUtil.set(CommonConstant.MUTIINSTANCE_NEXT_FINISH + execution.getProcessInstanceId(), execution.getProcessInstanceId());
             			}
             			if(nrOfCompletedInstances >= nrOfInstances) {
             				redisUtil.removeAll(CommonConstant.MUTIINSTANCE_NEXT_FINISH + execution.getProcessInstanceId());
             			}
             		}
             		else if(multiInstance.getCompletionCondition().startsWith("${nrOfCompletedInstances/nrOfInstances>=")) {
             			int index = multiInstance.getCompletionCondition().indexOf("=");
             			String after = multiInstance.getCompletionCondition().substring(index+1);
             			double ratio = Double.valueOf(after);
             			
             			if((nrOfCompletedInstances/nrOfInstances + 1) >= ratio){//结束会签流程前一个节点提示进行用户选择
             				redisUtil.set(CommonConstant.MUTIINSTANCE_NEXT_FINISH + execution.getProcessInstanceId(), execution.getProcessInstanceId());
             			}
             			if(nrOfCompletedInstances/nrOfInstances >= ratio) {
             				redisUtil.removeAll(CommonConstant.MUTIINSTANCE_NEXT_FINISH + execution.getProcessInstanceId());
             			}
             		}
             		/*log.info(execution.getId() + " - " + execution.getProcessInstanceId()
      			      + " - " + execution.getEventName()
      			      + " - " + execution.getCurrentActivityId()
      			      + " - " + execution.getProcessInstanceBusinessKey());
             		log.info("UserExecutionListener会签方式：" + multiInstance.isSequential());
                 	log.info("UserExecutionListener会签条件：" + multiInstance.getCompletionCondition());
        	        log.info("UserExecutionListener总会签数：" + execution.getVariable("nrOfInstances"));
        	        log.info("UserExecutionListener激活的会签数：" + execution.getVariable("nrOfActiveInstances"));
        	        log.info("UserExecutionListener已经完成会签数：" + execution.getVariable("nrOfCompletedInstances"));*/
         		}
         	 }
         	
		  }	
		}
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContextNew) throws BeansException {
		// TODO Auto-generated method stub
		applicationContext = applicationContextNew;
	}
}
