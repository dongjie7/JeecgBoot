package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkflowRule;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务工作流规则表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
public interface ITwTaskWorkflowRuleService extends IService<TwTaskWorkflowRule> {

	Result<?> getRules(String workflowId);

}
