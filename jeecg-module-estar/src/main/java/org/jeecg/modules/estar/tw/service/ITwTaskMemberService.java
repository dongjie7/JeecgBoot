package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskMember;

import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目任务团队表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwTaskMemberService extends IService<TwTaskMember> {
	public Result<?> inviteMember(String memberId,String taskId,Integer isExecutor,Integer isOwner,boolean fromCreate ,boolean isRobot);
	public IPage<Map> getTaskMemberByTaskId(IPage iPage,String taskId);
	public Result<?> inviteMemberBatch(Map<String, Object> mmap);
}
