package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectMember;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目成员
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwProjectMemberService extends IService<TwProjectMember> {

	void addMember(String projectId, String userId);

	void removeMember(String projectId, String userId);

	List<Map> listForAdd(String projectId,String organizationId);
	
	public boolean isProjectMember(String projectId, String userId);

	List<Map> searchMember(String projectId, String organizationId, String keyword);

	List<Map> listByProjectId(String projectId);

	Map gettMemberIdAndNameByProjectId(String id);

	Result<?> inviteMember(String memberId, String projectId, Integer isOwner);

	Result<?> listForInvite(Map<String, Object> mmap);

	Result<?> searchInviteMember(Map<String, Object> mmap);
   

}
