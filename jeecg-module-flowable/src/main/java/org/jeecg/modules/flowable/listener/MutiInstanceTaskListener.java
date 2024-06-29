package org.jeecg.modules.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 多实例会签任务全局监听器
 * @author nbacheng
 * @date 2022-09-22
*/
@Slf4j
@Component
public class MutiInstanceTaskListener implements TaskListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void notify(DelegateTask delegateTask) {

		//execution.setVariable("approval", Arrays.asList("张三","李四","王五"));//这里也可以设置会签人员 
	
    }
}
