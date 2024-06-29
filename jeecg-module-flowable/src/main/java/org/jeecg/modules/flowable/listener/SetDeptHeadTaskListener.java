package org.jeecg.modules.flowable.listener;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.delegate.DelegateTask;
import org.jeecg.common.util.SpringContextUtils;

import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * 根据发起人动态设置发起人的部门领导
 * @author nbacheng
 * @date 2023-3-02
*/

public class SetDeptHeadTaskListener implements TaskListener{

	private static final long serialVersionUID = 1L;
	private TaskService taskService = SpringContextUtils.getBean(TaskService.class);
	private IFlowThirdService flowThirdService = SpringContextUtils.getBean(IFlowThirdService.class);
	RuntimeService runtimeService = SpringContextUtils.getBean(RuntimeService.class);
	
	@Override
	public void notify(DelegateTask delegateTask) {
		
		String taskId = delegateTask.getId();

        // 获取流程发起人
		ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
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
		List<String> listDistinctResult = new ArrayList<String>();
		if(!DepHeadlist.isEmpty() ) {
		    for (String str : DepHeadlist) {
		        if (!listDistinctResult.contains(str)) {
		        	listDistinctResult.add(str);
		        }
		    }
		}
		
		Map<String, Object> map = taskService.getVariables(taskId);
		map.put("SetDeptHeadTaskListener", listDistinctResult);
		taskService.setVariables(taskId, map);
	}
}
