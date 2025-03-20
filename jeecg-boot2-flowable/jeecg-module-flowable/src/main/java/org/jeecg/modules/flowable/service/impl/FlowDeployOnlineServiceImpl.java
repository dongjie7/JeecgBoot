package org.jeecg.modules.flowable.service.impl;

import org.jeecg.modules.flowable.entity.FlowDeployOnline;
import org.jeecg.modules.flowable.mapper.FlowDeployOnlineMapper;
import org.jeecg.modules.flowable.service.IFlowDeployOnlineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: flow_deploy_online
 * @Author: nbacheng
 * @Date:   2022-10-21
 * @Version: V1.0
 */
@Service
public class FlowDeployOnlineServiceImpl extends ServiceImpl<FlowDeployOnlineMapper, FlowDeployOnline> implements IFlowDeployOnlineService {

	@Autowired
	FlowDeployOnlineMapper flowDeployOnlineMapper;
	@Override
	public FlowDeployOnline selectFlowDeployOnlineByDeployId(String deployId) {
		// TODO Auto-generated method stub
		return flowDeployOnlineMapper.selectFlowDeployOnlineByDeployId(deployId);
	}
	@Override
	public FlowDeployOnline selectFlowDeployOnlineByOnlineIdDeployId(String onlineId, String deployId) {
		// TODO Auto-generated method stub
		return flowDeployOnlineMapper.selectFlowDeployOnlineByOnlineIdDeployId(onlineId, deployId);
	}

}
