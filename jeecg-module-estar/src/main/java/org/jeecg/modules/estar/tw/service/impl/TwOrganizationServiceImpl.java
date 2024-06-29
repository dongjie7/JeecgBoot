package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwOrganization;
import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwAccount;
import org.jeecg.modules.estar.tw.mapper.TwOrganizationMapper;
import org.jeecg.modules.estar.tw.mapper.TwProjectMapper;
import org.jeecg.modules.estar.tw.mapper.TwAccountMapper;
import org.jeecg.modules.estar.tw.service.ITwOrganizationService;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目组织表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Service
public class TwOrganizationServiceImpl extends ServiceImpl<TwOrganizationMapper, TwOrganization> implements ITwOrganizationService {

	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Autowired
	ITwAccountService iAccountService;
	
	@Autowired
	TwProjectMapper projectMapper;
	
	@Autowired
	TwAccountMapper accountMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveAddCcount(TwOrganization twOrganization) {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        save(twOrganization);
        TwAccount account= new TwAccount();
        account.setOrganizationId(twOrganization.getId());
        account.setUserId(userId);
        account.setName(loginUser.getRealname());
        account.setEmail(loginUser.getEmail());
        account.setIsOwner(1);
        account.setStatus(1);
        iAccountService.save(account);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delRemoveAccount(String id) {
		
		List<TwProject> listProject = projectMapper.selectProjectByOrgId(id);
		if(CollectionUtils.isNotEmpty(listProject)){
			return false;
		}
		else {
			removeById(id);
			accountMapper.deleteAccount(id);
			return true;
		}
	}

}
