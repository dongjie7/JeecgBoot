package org.jeecg.modules.erp.sale.service;

import org.jeecg.modules.erp.sale.entity.ErpSaleOutDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售出库单明细
 * @Author: nbacheng
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface IErpSaleOutDetailService extends IService<ErpSaleOutDetail> {

	public List<ErpSaleOutDetail> selectByMainId(String mainId);
}
