package org.jeecg.modules.erp.goods.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.erp.goods.entity.ErpGoodsCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: erp_goods_category
 * @Author: nbacheng
 * @Date:   2022-08-28
 * @Version: V1.0
 */
public interface ErpGoodsCategoryMapper extends BaseMapper<ErpGoodsCategory> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
