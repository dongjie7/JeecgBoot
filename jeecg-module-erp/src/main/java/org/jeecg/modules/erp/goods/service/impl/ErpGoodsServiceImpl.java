package org.jeecg.modules.erp.goods.service.impl;

import org.jeecg.modules.erp.goods.entity.ErpGoods;
import org.jeecg.modules.erp.goods.entity.ErpGoodsPrice;
import org.jeecg.modules.erp.goods.mapper.ErpGoodsPriceMapper;
import org.jeecg.modules.erp.goods.mapper.ErpGoodsMapper;
import org.jeecg.modules.erp.goods.service.IErpGoodsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 商品基础信息
 * @Author: nbacheng
 * @Date:   2023-03-08
 * @Version: V1.0
 */
@Service
public class ErpGoodsServiceImpl extends ServiceImpl<ErpGoodsMapper, ErpGoods> implements IErpGoodsService {

	@Autowired
	private ErpGoodsMapper erpGoodsMapper;
	@Autowired
	private ErpGoodsPriceMapper erpGoodsPriceMapper;
	
	@Override
	@Transactional
	public void saveMain(ErpGoods erpGoods, List<ErpGoodsPrice> erpGoodsPriceList) {
		erpGoodsMapper.insert(erpGoods);
		if(erpGoodsPriceList!=null && erpGoodsPriceList.size()>0) {
			for(ErpGoodsPrice entity:erpGoodsPriceList) {
				//外键设置
				entity.setId(erpGoods.getId());
				erpGoodsPriceMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ErpGoods erpGoods,List<ErpGoodsPrice> erpGoodsPriceList) {
		erpGoodsMapper.updateById(erpGoods);
		
		//1.先删除子表数据
		erpGoodsPriceMapper.deleteByMainId(erpGoods.getId());
		
		//2.子表数据重新插入
		if(erpGoodsPriceList!=null && erpGoodsPriceList.size()>0) {
			for(ErpGoodsPrice entity:erpGoodsPriceList) {
				//外键设置
				entity.setId(erpGoods.getId());
				erpGoodsPriceMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		erpGoodsPriceMapper.deleteByMainId(id);
		erpGoodsMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			erpGoodsPriceMapper.deleteByMainId(id.toString());
			erpGoodsMapper.deleteById(id);
		}
	}
	
}
