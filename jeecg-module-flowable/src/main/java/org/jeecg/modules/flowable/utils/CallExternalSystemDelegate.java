package org.jeecg.modules.flowable.utils;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class CallExternalSystemDelegate implements JavaDelegate {
	public void execute(DelegateExecution execution) {
		System.out.println("Calling the external system " + execution.getVariable("assignee"));
	}
}
