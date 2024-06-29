package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 收藏表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwCollectionMapper extends BaseMapper<TwCollection> {
	@Select("SELECT * FROM `tw_collection` WHERE `source_id` = #{sourceId} AND `type` = 'task' AND `member_id` = #{memberId} LIMIT 1")
    Map selectCollection(@Param("sourceId") String sourceId,@Param("memberId") String memberId);
}
