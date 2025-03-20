package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwAccount;
import org.jeecg.modules.estar.tw.entity.TwProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 团队成员
 * @Author: nbacheng
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface TwAccountMapper extends BaseMapper<TwAccount> {
  @Delete("DELETE FROM tw_account WHERE organization_id = #{id}")
  void deleteAccount(@Param("id") String id);
  
  //根据用户id获取组织ID
  @Select("SELECT organization_id FROM tw_account WHERE status=1 AND user_id = #{userId}")
  String selectOrgIdByUserId(@Param("userId") String userId);
  
  @Select("SELECT * FROM tw_account A WHERE A.name LIKE CONCAT('%',#{name},'%') AND A.organization_id = #{organizationId}")
  List<Map> getMemberByName(@Param("organizationId") String organizationId,@Param("name") String name);
  
  @Select("SELECT * FROM tw_account WHERE user_id = #{memberId} AND organization_id = #{orgId} LIMIT 1")
  Map selectMemberAccountByMemIdAndOrgId(@Param("memberId") String memberId,@Param("orgId") String orgId);
}
