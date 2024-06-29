package org.jeecg.modules.flowable.service;

import org.jeecg.modules.flowable.entity.FlowOnlCgformHead;
import org.jeecg.modules.flowable.entity.vo.OnlCgformDataVo;

import java.util.Map;

import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.config.exception.BusinessException;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: onl_cgform_head
 * @Author: nbacheng
 * @Date:   2022-10-22
 * @Version: V1.0
 */
public interface IFlowOnlCgformHeadService extends IService<FlowOnlCgformHead> {
	//根据formId查询online表单信息，这里的formId就是online表id主键
	public Map<String, Object> getOnlCgformHeadByFormId(String formId);
	//根据录入的online表单数据保存
	public void save(OnlCgformDataVo onlCgformDataVo) throws BusinessException;
}
