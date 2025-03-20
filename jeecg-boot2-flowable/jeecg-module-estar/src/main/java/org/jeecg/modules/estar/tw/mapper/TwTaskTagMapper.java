package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwTaskTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务标签表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwTaskTagMapper extends BaseMapper<TwTaskTag> {

	@Select("SELECT * FROM tw_task_tag WHERE project_id = #{projectId} ORDER BY name ASC")
    List<Map> selectTaskTagByProjectId(@Param("projectId") String projectId);

    @Select("SELECT * FROM tw_task_tag WHERE id = #{id} LIMIT 1")
    Map selectTaskTagById(@Param("id") String id);

    @Select("SELECT * FROM tw_task_tag WHERE name = #{params.name} AND project_id = #{params.projectId} LIMIT 1")
    Map selectTaskTagByNameAndProjectId(@Param("params") Map params);
}
