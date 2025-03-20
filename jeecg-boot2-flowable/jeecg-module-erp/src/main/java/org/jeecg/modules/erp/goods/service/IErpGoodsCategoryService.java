package org.jeecg.modules.erp.goods.service;

import org.jeecg.modules.erp.goods.entity.ErpGoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: erp_goods_category
 * @Author: nbacheng
 * @Date:   2022-08-28
 * @Version: V1.0
 */
public interface IErpGoodsCategoryService extends IService<ErpGoodsCategory> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addErpGoodsCategory(ErpGoodsCategory erpGoodsCategory);
	
	/**修改节点*/
	void updateErpGoodsCategory(ErpGoodsCategory erpGoodsCategory) throws JeecgBootException;
	
	/**删除节点*/
	void deleteErpGoodsCategory(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<ErpGoodsCategory> queryTreeListNoPage(QueryWrapper<ErpGoodsCategory> queryWrapper);

}
