package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkflow;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务工作流表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
public interface ITwTaskWorkflowService extends IService<TwTaskWorkflow> {

	@SuppressWarnings("rawtypes")
	List<Map> getList(String projectId);

	boolean saveAndRules(String projectId, String organizationId, String taskWorkflowName, String taskWorkflowRules);

	boolean editAndRules(String id, String taskWorkflowName, String taskWorkflowRules);

	boolean removeAndRules(String id);

	void queryRule(String projectId, String stageId, String taskId, String memberId, Integer action);

}
