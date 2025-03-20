package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwSourceLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目资源关联表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwSourceLinkMapper extends BaseMapper<TwSourceLink> {
	@Select("SELECT id,source_type,source_id,link_type,link_id,organization_id,create_by,create_time,sort FROM tw_source_link WHERE link_id = #{linkId} AND link_type = #{linkType} ORDER BY id DESC")
    List<Map> selectSourceLinkByLinkIdAndType(@Param("linkId") String linkId, @Param("linkType") String linkType);
}
