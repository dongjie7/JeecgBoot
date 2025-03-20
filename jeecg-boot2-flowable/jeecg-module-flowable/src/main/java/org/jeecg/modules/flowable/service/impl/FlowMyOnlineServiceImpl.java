package org.jeecg.modules.flowable.service.impl;

import org.jeecg.modules.flowable.entity.FlowMyOnline;
import org.jeecg.modules.flowable.mapper.FlowMyOnlineMapper;
import org.jeecg.modules.flowable.service.IFlowMyOnlineService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: flow_my_online
 * @Author: nbacheng
 * @Date:   2022-11-02
 * @Version: V1.0
 */
@Service
public class FlowMyOnlineServiceImpl extends ServiceImpl<FlowMyOnlineMapper, FlowMyOnline> implements IFlowMyOnlineService {

	public FlowMyOnline getByDataId(String dataId) {
        LambdaQueryWrapper<FlowMyOnline> flowMyOnlineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowMyOnlineLambdaQueryWrapper.eq(FlowMyOnline::getDataId,dataId)
        ;
        //如果保存数据前未调用必调的FlowCommonService.initActBusiness方法，就会有问题
        FlowMyOnline online = this.getOne(flowMyOnlineLambdaQueryWrapper);
        return online;
    }
}
