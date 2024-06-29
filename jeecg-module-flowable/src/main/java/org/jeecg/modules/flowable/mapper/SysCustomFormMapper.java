package org.jeecg.modules.flowable.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.flowable.domain.vo.CustomFormVo;
import org.jeecg.modules.flowable.entity.SysCustomForm;

/**
 * @Description: 系统自定义表单表
 * @Author: nbacheng
 * @Date:   2022-04-23
 * @Version: V1.0
 */
public interface SysCustomFormMapper extends BaseMapper<SysCustomForm> {
	SysCustomForm selectSysCustomFormById(String formId);
	SysCustomForm selectSysCustomFormByServiceName(String serviceName);
	void updateCustom(@Param("customFormVo") CustomFormVo customFormVo);
}
