package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwTaskToTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务标签映射表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwTaskToTagMapper extends BaseMapper<TwTaskToTag> {

	@Select("SELECT * FROM tw_task_to_tag WHERE task_id = #{taskId}")
	List<Map> selectTaskToTagByTaskId(@Param("taskId") String taskId);

    @Select("SELECT * FROM tw_task_to_tag WHERE tag_id = #{tagId} AND task_id = #{taskId} LIMIT 1")
    Map selectTaskToTagByTagIdAndTaskId(@Param("tagId") String tagId,@Param("taskId") String taskId);
}
