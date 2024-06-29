package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkflow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务工作流表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
public interface TwTaskWorkflowMapper extends BaseMapper<TwTaskWorkflow> {
	@SuppressWarnings("rawtypes")
	@Select("SELECT * FROM tw_task_workflow a WHERE a.project_id = #{projectId} ORDER BY a.id ASC")
    List<Map> getWorkflowByProjectId(@Param("projectId") String projectId);
}
