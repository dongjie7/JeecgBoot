package org.jeecg.modules.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.flowable.entity.SysForm;

/**
 * @Description: 系统流程表单
 * @Author: nbacheng
 * @Date:   2022-04-07
 * @Version: V1.0
 */
public interface ISysFormService extends IService<SysForm> {

	SysForm selectSysFormById(String formId);

}
