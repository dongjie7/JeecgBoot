package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkflowRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务工作流规则表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
public interface TwTaskWorkflowRuleMapper extends BaseMapper<TwTaskWorkflowRule> {
	@SuppressWarnings("rawtypes")
	@Select("SELECT * FROM tw_task_workflow_rule a WHERE a.workflow_id = #{workflowId} ORDER BY sort ASC")
    List<Map> getRuleByWorkflowId(@Param("workflowId") String workflowId);
}
