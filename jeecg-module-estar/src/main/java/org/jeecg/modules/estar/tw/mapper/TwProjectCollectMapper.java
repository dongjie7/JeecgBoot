package org.jeecg.modules.estar.tw.mapper;

import org.jeecg.modules.estar.tw.entity.TwProjectCollect;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目收藏表
 * @Author: nbacheng
 * @Date:   2023-06-09
 * @Version: V1.0
 */
public interface TwProjectCollectMapper extends BaseMapper<TwProjectCollect> {

	@SuppressWarnings("rawtypes")
	@Select("SELECT * FROM tw_project_collect A  WHERE A.project_id = #{projectId} and A.user_id = #{memberId}")
    List<Map> selectProjectCollection(@Param("projectId") String projectId,@Param("memberId")  String memberId);

}
