package org.jeecg.modules.estar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.entity.OaSalaryapproval;
import org.jeecg.modules.estar.mapper.OaSalaryapprovalMapper;
import org.jeecg.modules.estar.service.IOaSalaryapprovalService;
import org.jeecg.modules.estar.vo.ProcessUpdateVo;

/**
 * @Description: 薪资审批表
 * @Author: nbacheng
 * @Date:   2022-03-03
 * @Version: V1.0
 */
@Service
public class OaSalaryapprovalServiceImpl extends ServiceImpl<OaSalaryapprovalMapper, OaSalaryapproval> implements IOaSalaryapprovalService {
   
	@Autowired
	OaSalaryapprovalMapper oaSalaryapprovalMapper;
	
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessStatus(ProcessUpdateVo processUpdateVo)
    {
	    oaSalaryapprovalMapper.updateProcessStatus(processUpdateVo);
    }
    
    @Override
    public OaSalaryapproval getByInstanceId(String processInstanceId)
    {
	    return oaSalaryapprovalMapper.getByInstanceId(processInstanceId);
   }
}
