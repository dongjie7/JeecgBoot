package org.jeecg.modules.flowable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.flowable.entity.BpmToolDesigner;
import org.jeecg.modules.flowable.entity.BpmToolRule;
import org.jeecg.modules.flowable.mapper.BpmToolDesignerMapper;
import org.jeecg.modules.flowable.mapper.BpmToolRuleMapper;
import org.jeecg.modules.flowable.service.BpmToolDesignerService;
import org.jeecg.modules.flowable.service.BpmToolRuleService;
import org.springframework.stereotype.Service;

/**
 * @author: WangYuZhou
 * @create: 2022-09-19 17:31
 * @description:
 **/

@Service
public class BpmToolRuleServiceImpl extends ServiceImpl<BpmToolRuleMapper, BpmToolRule> implements BpmToolRuleService {
}
