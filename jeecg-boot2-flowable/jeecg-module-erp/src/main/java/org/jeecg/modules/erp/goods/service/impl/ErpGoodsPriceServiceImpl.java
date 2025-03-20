package org.jeecg.modules.erp.goods.service.impl;

import org.jeecg.modules.erp.goods.entity.ErpGoodsPrice;
import org.jeecg.modules.erp.goods.mapper.ErpGoodsPriceMapper;
import org.jeecg.modules.erp.goods.service.IErpGoodsPriceService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 商品价格信息
 * @Author: nbacheng
 * @Date:   2023-03-08
 * @Version: V1.0
 */
@Service
public class ErpGoodsPriceServiceImpl extends ServiceImpl<ErpGoodsPriceMapper, ErpGoodsPrice> implements IErpGoodsPriceService {
	
	@Autowired
	private ErpGoodsPriceMapper erpGoodsPriceMapper;
	
	@Override
	public List<ErpGoodsPrice> selectByMainId(String mainId) {
		return erpGoodsPriceMapper.selectByMainId(mainId);
	}
}
