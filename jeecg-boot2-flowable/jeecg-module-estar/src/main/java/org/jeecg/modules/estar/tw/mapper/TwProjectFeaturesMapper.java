package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwProjectFeatures;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 版本库表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwProjectFeaturesMapper extends BaseMapper<TwProjectFeatures> {
	@Select("SELECT * FROM tw_project_features WHERE project_id = #{projectId} ORDER BY id DESC")
    List<Map> selectProjectFeaturesByprojectId(@Param("projectId") String projectId);

    @Select("SELECT * FROM tw_project_features WHERE id = #{id} ")
    Map selectProjectFeaturesById(@Param("id") String id);

    @Select("SELECT * FROM tw_project_features WHERE name = #{name} AND project_id = #{projectId} LIMIT 1 ")
    Map selectProjectFeaturesOneByNameAndprojectId(@Param("name") String name,@Param("projectId") String projectId);

    @Delete("DELETE FROM tw_project_features WHERE id = #{id}")
    Integer deleteProjectFeaturesById(@Param("id") String id);
}
