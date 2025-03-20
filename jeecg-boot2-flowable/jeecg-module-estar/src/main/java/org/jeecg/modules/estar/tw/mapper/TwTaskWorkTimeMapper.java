package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务工时表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface TwTaskWorkTimeMapper extends BaseMapper<TwTaskWorkTime> {

	@Select("SELECT * FROM tw_task_work_time WHERE task_id = #{taskId}")
    List<Map> selectTaskWorkTimeByTaskId(@Param("taskId") String taskId);

    @Select("SELECT * FROM tw_task_work_time WHERE id = #{id}")
    Map selectTaskWorkTimeById(@Param("id") String id);

    @Update("DELETE FROM team_task_work_time WHERE id = #{id}")
    Integer deleteTaskWorkTimeById(@Param("id") String id);
}
