package org.jeecg.modules.estar.tw.mapper;

import org.jeecg.modules.estar.tw.entity.TwProjectMember;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目成员
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface TwProjectMemberMapper extends BaseMapper<TwProjectMember> {
	@SuppressWarnings("rawtypes")
	@Select("SELECT user_id, realname FROM tw_project_member pm LEFT JOIN sys_user m ON pm.user_id = m.username WHERE pm.project_id = #{projectId} AND is_owner = 1 LIMIT 1")
    Map selectMemberIdAndNameByProjectId(@Param("projectId") String projectId);
	@Select("SELECT * FROM tw_member_account A WHERE A.name LIKE CONCAT('%',#{name},'%') AND A.organization_id = #{orgId}")
    List<Map> getMemberCountByOrgIdAndMemberName(@Param("orgId") String orgId,@Param("name") String name);
}
