package org.jeecg.modules.flowable.service;

import org.jeecg.modules.flowable.entity.FlowDeployOnline;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: flow_deploy_online
 * @Author: nbacheng
 * @Date:   2022-10-21
 * @Version: V1.0
 */
public interface IFlowDeployOnlineService extends IService<FlowDeployOnline> {
	/**
     * 查询流程挂着的online表单
     * @param deployId
     * @return
     */
	FlowDeployOnline selectFlowDeployOnlineByDeployId(String deployId);    
	
	/**
     * 查询流程挂着的deployId
     * @param onlineId
     * @return
     */
	FlowDeployOnline selectFlowDeployOnlineByOnlineIdDeployId(String onlineId,String deployId);      
}
