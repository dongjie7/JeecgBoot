package org.jeecg.modules.flowable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.jeecg.modules.flowable.entity.SysCustomForm;
import org.jeecg.modules.flowable.entity.SysDeployForm;
import org.jeecg.modules.flowable.entity.SysForm;
import org.jeecg.modules.flowable.mapper.SysDeployFormMapper;
import org.jeecg.modules.flowable.service.ISysDeployFormService;

/**
 * @Description: 流程实例关联表单
 * @Author: nbacheng
 * @Date:   2022-04-11
 * @Version: V1.0
 */
@Service
public class SysDeployFormServiceImpl extends ServiceImpl<SysDeployFormMapper, SysDeployForm> implements ISysDeployFormService {

	@Autowired
    private SysDeployFormMapper sysDeployFormMapper;
	
	@Override
	public SysForm selectSysDeployFormByDeployId(String deployId) {
		// TODO Auto-generated method stub
		return sysDeployFormMapper.selectSysDeployFormByDeployId(deployId);
	}
	
	@Override
	public SysCustomForm selectSysCustomFormByDeployId(String deployId) {
		// TODO Auto-generated method stub
		return sysDeployFormMapper.selectSysCustomFormByDeployId(deployId);
	}

	@Override
	public SysDeployForm selectSysDeployFormByFormId(String formId) {
		// TODO Auto-generated method stub
		return sysDeployFormMapper.selectSysDeployFormByFormId(formId);
	}

	@Override
	public boolean insertBatch(List<SysDeployForm> deployFormList) {
		/**
	     * 批量插入(包含限制条数,目前现在100条)
	     */
	    String sqlStatement = SqlHelper.getSqlStatement(this.currentMapperClass(), SqlMethod.INSERT_ONE);
	      return SqlHelper.executeBatch(this.currentModelClass(), log, deployFormList, 100,
	            (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
	}

	@Override
	public SysForm selectCurSysDeployForm(String formId, String deployId, String nodeKey) {
		// TODO Auto-generated method stub
		return sysDeployFormMapper.selectCurSysDeployForm(formId, deployId, nodeKey);
	}

}
