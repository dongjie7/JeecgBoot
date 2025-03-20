package org.jeecg.modules.erp.goods.service;

import org.jeecg.modules.erp.goods.entity.ErpGoodsPrice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 商品价格信息
 * @Author: nbacheng
 * @Date:   2023-03-08
 * @Version: V1.0
 */
public interface IErpGoodsPriceService extends IService<ErpGoodsPrice> {

	public List<ErpGoodsPrice> selectByMainId(String mainId);
}
