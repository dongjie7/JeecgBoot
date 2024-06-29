package org.jeecg.modules.estar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.estar.entity.OaSalaryapproval;
import org.jeecg.modules.estar.vo.ProcessUpdateVo;

/**
 * @Description: 薪资审批表
 * @Author: nbacheng
 * @Date:   2022-03-03
 * @Version: V1.0
 */
public interface IOaSalaryapprovalService extends IService<OaSalaryapproval> {

	void updateProcessStatus(ProcessUpdateVo processUpdateVo);

	OaSalaryapproval getByInstanceId(String processInstanceId);

}
