package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwAccount;

import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 团队成员
 * @Author: nbacheng
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwAccountService extends IService<TwAccount> {
	String selectOrgIdByUserId(String userId);
	public TwAccount inviteMember(TwAccount account);
	public  Map getMemberAccountByMemIdAndOrgId(String memberId,String orgId);
	Result<?> accountAdd(TwAccount twAccount);
	Result<?> accountEdit(TwAccount twAccount);
}
