package org.jeecg.modules.flowable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.flowable.entity.BpmToolTableBpmn;
import org.jeecg.modules.flowable.mapper.BpmToolTableBpmnInfoMapper;
import org.jeecg.modules.flowable.service.IBpmToolTableBpmnInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: WangYuZhou
 * @create: 2022-08-27 12:06
 * @description:
 **/
@Service
public class BpmToolTableBpmnInfoServiceImpl extends ServiceImpl<BpmToolTableBpmnInfoMapper, BpmToolTableBpmn> implements IBpmToolTableBpmnInfoService {

    @Autowired
    private BpmToolTableBpmnInfoMapper bpmToolTableBpmnInfoMapper;

    @Override
    public BpmToolTableBpmn selectBpmToolTableByBpmId(String bpmId) {
        return bpmToolTableBpmnInfoMapper.selectBpmToolTableByBpmId(bpmId);
    }
}
