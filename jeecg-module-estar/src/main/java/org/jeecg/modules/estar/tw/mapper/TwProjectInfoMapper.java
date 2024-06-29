package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwProjectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目自定义信息表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface TwProjectInfoMapper extends BaseMapper<TwProjectInfo> {
	@Select("SELECT * FROM tw_project_info WHERE project_id = #{projectId} ORDER BY id DESC")
    List<Map> selectProjectInfoByProjectId(@Param("projectId") String projectId);
}
