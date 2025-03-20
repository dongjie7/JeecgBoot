package org.jeecg.modules.erp.goods.service;

import org.jeecg.modules.erp.goods.entity.ErpGoodsPrice;
import org.jeecg.modules.erp.goods.entity.ErpGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 商品基础信息
 * @Author: nbacheng
 * @Date:   2023-03-08
 * @Version: V1.0
 */
public interface IErpGoodsService extends IService<ErpGoods> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ErpGoods erpGoods,List<ErpGoodsPrice> erpGoodsPriceList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ErpGoods erpGoods,List<ErpGoodsPrice> erpGoodsPriceList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
