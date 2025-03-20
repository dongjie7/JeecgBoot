package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkflowRule;
import org.jeecg.modules.estar.tw.mapper.TwTaskWorkflowRuleMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkflowRuleService;

import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务工作流规则表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
@Service
public class TwTaskWorkflowRuleServiceImpl extends ServiceImpl<TwTaskWorkflowRuleMapper, TwTaskWorkflowRule> implements ITwTaskWorkflowRuleService {

	@Autowired
	TwTaskWorkflowRuleMapper taskWorkflowRuleMapper;
	@Override
	public Result<?> getRules(String workflowId) {
		return Result.OK(taskWorkflowRuleMapper.getRuleByWorkflowId(workflowId));
	}

}
