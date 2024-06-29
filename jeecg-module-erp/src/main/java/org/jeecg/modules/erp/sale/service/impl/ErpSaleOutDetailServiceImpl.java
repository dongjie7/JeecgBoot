package org.jeecg.modules.erp.sale.service.impl;

import org.jeecg.modules.erp.sale.entity.ErpSaleOutDetail;
import org.jeecg.modules.erp.sale.mapper.ErpSaleOutDetailMapper;
import org.jeecg.modules.erp.sale.service.IErpSaleOutDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售出库单明细
 * @Author: nbacheng
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Service
public class ErpSaleOutDetailServiceImpl extends ServiceImpl<ErpSaleOutDetailMapper, ErpSaleOutDetail> implements IErpSaleOutDetailService {
	
	@Autowired
	private ErpSaleOutDetailMapper erpSaleOutDetailMapper;
	
	@Override
	public List<ErpSaleOutDetail> selectByMainId(String mainId) {
		return erpSaleOutDetailMapper.selectByMainId(mainId);
	}
}
