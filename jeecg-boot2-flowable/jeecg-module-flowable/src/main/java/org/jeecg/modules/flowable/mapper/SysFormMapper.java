package org.jeecg.modules.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.flowable.entity.SysForm;

/**
 * @Description: 系统流程表单
 * @Author: nbacheng
 * @Date:   2022-04-07
 * @Version: V1.0
 */
public interface SysFormMapper extends BaseMapper<SysForm> {

	SysForm selectSysFormById(String formId);

}
