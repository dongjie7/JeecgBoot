package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwProjectVersionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 项目版本日志表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface TwProjectVersionLogMapper extends BaseMapper<TwProjectVersionLog> {
	@Select("SELECT * FROM tw_project_version_log WHERE source_id = #{sourceId} ORDER BY id ASC")
    IPage<Map> selectProjectVersionLogBySourceId(IPage<Map> page, @Param("sourceId") String sourceId);

    @Select("SELECT * FROM tw_project_version_log WHERE source_id = #{sourceId} ORDER BY id ASC")
    List<Map> selectProjectVersionLogBySourceIdAll(@Param("sourceId") String sourceId);
}
