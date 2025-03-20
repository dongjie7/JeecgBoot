package org.jeecg.modules.erp.sale.service;

import org.jeecg.modules.erp.sale.entity.ErpSaleOutDetail;
import org.jeecg.modules.erp.sale.entity.ErpSaleOut;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.jeecg.common.api.vo.Result;

/**
 * @Description: 销售出库单
 * @Author: nbacheng
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface IErpSaleOutService extends IService<ErpSaleOut> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ErpSaleOut erpSaleOut,List<ErpSaleOutDetail> erpSaleOutDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ErpSaleOut erpSaleOut,List<ErpSaleOutDetail> erpSaleOutDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 
	 * @param id
	 * @return 
	 * @throws Exception 
	 */
	public Result<?> approvePass(String id) throws Exception;
	
}
