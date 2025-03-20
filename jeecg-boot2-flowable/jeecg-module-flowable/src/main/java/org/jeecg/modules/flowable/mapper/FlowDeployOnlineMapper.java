package org.jeecg.modules.flowable.mapper;


import org.jeecg.modules.flowable.entity.FlowDeployOnline;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: flow_deploy_online
 * @Author: nbacheng
 * @Date:   2022-10-21
 * @Version: V1.0
 */
public interface FlowDeployOnlineMapper extends BaseMapper<FlowDeployOnline> {
	 /**
     * 查询流程挂着的online表单
     * @param deployId
     * @return
     */
	FlowDeployOnline selectFlowDeployOnlineByDeployId(@Param("deployId") String deployId);   
	
	/**
     * 查询流程挂着的数据
     * @param onlineId，deployId
     * @return
     */
	FlowDeployOnline selectFlowDeployOnlineByOnlineIdDeployId(@Param("onlineId") String onlineId,@Param("deployId") String deployId);      
}
