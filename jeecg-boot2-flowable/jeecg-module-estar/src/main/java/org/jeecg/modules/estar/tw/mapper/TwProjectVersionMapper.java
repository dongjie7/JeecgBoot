package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwProjectVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目版本表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwProjectVersionMapper extends BaseMapper<TwProjectVersion> {
	@Select("SELECT * FROM tw_project_version WHERE features_id = #{featuresId} ORDER BY id ASC")
    List<Map> selectProjectVersionByFeaturesId(@Param("featuresId") String featuresId);

    @Select("SELECT * FROM tw_project_version WHERE id = #{id} ")
    Map selectProjectVersionById(@Param("id") String id);

    @Select("SELECT * FROM tw_project_version WHERE name = #{name} AND features_id = #{featuresId} LIMIT 1")
    Map selectProjectVersionByNameAndFeaturesId(@Param("name") String name,@Param("featuresId") String featuresId);

    @Delete("DELETE FROM tw_project_version WHERE id = #{id}")
    Integer deleteProjectVersionById(@Param("id") String id);
}
