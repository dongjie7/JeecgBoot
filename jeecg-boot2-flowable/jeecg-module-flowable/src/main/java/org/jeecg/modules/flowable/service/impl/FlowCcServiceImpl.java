package org.jeecg.modules.flowable.service.impl;

import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;
import org.jeecg.modules.flowable.domain.vo.FlowTaskVo;
import org.jeecg.modules.flowable.entity.FlowCc;
import org.jeecg.modules.flowable.mapper.FlowCcMapper;
import org.jeecg.modules.flowable.service.IFlowCcService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * @Description: 流程抄送表
 * @Author: nbacheng
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@Service
public class FlowCcServiceImpl extends ServiceImpl<FlowCcMapper, FlowCc> implements IFlowCcService {
	
	@Resource
    private IFlowThirdService iFlowThirdService;
	
	@Autowired
	private HistoryService historyService;

	@Resource
	private FlowCcMapper flowCcMapper;

	@Override
	public Boolean flowCc(FlowTaskVo taskVo) {
		if (StringUtils.isEmpty(taskVo.getCcUsers())) {
            // 若抄送用户为空，则不需要处理，返回成功
            return true;
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(taskVo.getInstanceId()).singleResult();
        String[] usernames = taskVo.getCcUsers().split(",");
        List<FlowCc> flowCcList = new ArrayList<>();
        String initiatorUsername = iFlowThirdService.getLoginUser().getUsername();
        String initiatorRealname = iFlowThirdService.getLoginUser().getRealname();
        String title = historicProcessInstance.getProcessDefinitionName();
        for (String username : usernames) {
			SysUser sysUser = iFlowThirdService.getUserByUsername(username);
        	FlowCc flowCc = new FlowCc();
        	flowCc.setTitle(title);
        	flowCc.setFlowId(historicProcessInstance.getProcessDefinitionId());
        	flowCc.setFlowName(historicProcessInstance.getProcessDefinitionName());
        	flowCc.setDeploymentId(historicProcessInstance.getDeploymentId());
        	flowCc.setInstanceId(taskVo.getInstanceId());
        	flowCc.setTaskId(taskVo.getTaskId());
        	flowCc.setBusinessKey(taskVo.getBusinessKey());
        	flowCc.setUsername(username);
        	flowCc.setReceiveRealname(sysUser.getRealname());
        	flowCc.setInitiatorUsername(initiatorUsername);
        	flowCc.setInitiatorRealname(initiatorRealname);
        	flowCc.setCategory(taskVo.getCategory());
        	flowCc.setState("未读");
        	flowCcList.add(flowCc);
        }
        return insertBatch(flowCcList);
	}
    /**
     * 批量插入(包含限制条数,目前现在100条)
    */
	@Override
	public boolean insertBatch(List<FlowCc> flowCcList) {
		String sqlStatement = SqlHelper.getSqlStatement(this.currentMapperClass(), SqlMethod.INSERT_ONE);
		return SqlHelper.executeBatch(this.currentModelClass(), log, flowCcList, 100,
		        (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
	}

	@Override
	public void updateStatus(String id) {
		flowCcMapper.updateState(id);
	}

}
