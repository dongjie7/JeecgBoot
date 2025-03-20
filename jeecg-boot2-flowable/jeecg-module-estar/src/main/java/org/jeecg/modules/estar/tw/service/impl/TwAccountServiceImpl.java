package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwAccount;
import org.jeecg.modules.estar.tw.mapper.TwAccountMapper;
import org.jeecg.modules.estar.tw.service.ITwAccountService;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

/**
 * @Description: 团队成员
 * @Author: nbacheng
 * @Date:   2023-06-02
 * @Version: V1.0
 */
@Service
public class TwAccountServiceImpl extends ServiceImpl<TwAccountMapper, TwAccount> implements ITwAccountService {

	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	public String selectOrgIdByUserId(String userId) {
		return baseMapper.selectOrgIdByUserId(userId);
	}

	@Override
	public TwAccount inviteMember(TwAccount account) {
		TwAccount hasJoined =lambdaQuery().eq(TwAccount::getUserId,account.getUserId())
                .eq(TwAccount::getOrganizationId,account.getOrganizationId()).one();
        if(ObjectUtils.isNotEmpty(hasJoined) && ObjectUtils.isNotEmpty(hasJoined.getId())){
            return account;
        }
        SysUser memberDate = iEstarThirdService.getUserByUsername(account.getUserId());
        if(ObjectUtil.isEmpty(memberDate)){
        	account.setIsOwner(0);
            account.setStatus(1);
            account.setName(memberDate.getRealname());
            account.setEmail(memberDate.getEmail());
            save(account);
            return account;
        }
        return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getMemberAccountByMemIdAndOrgId(String memberId, String orgId) {
		return baseMapper.selectMemberAccountByMemIdAndOrgId(memberId, orgId);
	}

	@Override
	@Transactional
	public Result<?> accountAdd(TwAccount twAccount) {
		String[] userId = twAccount.getUserId().split(",");
		Collection<String> idList = Arrays.asList(userId);
		idList.forEach(item -> {
			SysUser sysUser = iEstarThirdService.getUserByUsername(item);
			TwAccount account = new TwAccount();
			account.setUserId(item);
			account.setOrganizationId(twAccount.getOrganizationId());
			account.setEmail(sysUser.getEmail());
			account.setName(sysUser.getRealname());
			account.setStatus(1);
			TwAccount hasaccount  = lambdaQuery().eq(TwAccount::getUserId,item).one();
			if(hasaccount == null) {
				save(account);
			}
		});
		return Result.OK("添加成功！");
	}

	@Override
	@Transactional
	public Result<?> accountEdit(TwAccount twAccount) {
	  SysUser sysUser = iEstarThirdService.getUserByUsername(twAccount.getUserId());
	  TwAccount account = new TwAccount();
	  account.setId(twAccount.getId());
	  account.setUserId(twAccount.getUserId());
	  account.setOrganizationId(twAccount.getOrganizationId());
	  account.setEmail(sysUser.getEmail());
	  account.setName(sysUser.getRealname());
	  updateById(account);
	  return Result.OK("编辑成功!");
	}

}
