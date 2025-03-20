package org.jeecg.modules.erp.stock.service.impl;


import org.jeecg.modules.erp.stock.entity.ErpGoodsStock;
import org.jeecg.modules.erp.stock.mapper.ErpGoodsStockMapper;
import org.jeecg.modules.erp.stock.service.IErpGoodsStockLogService;
import org.jeecg.modules.erp.stock.service.IErpGoodsStockService;
import org.jeecg.modules.erp.stock.vo.ErpGoodsStockVo;
import org.jeecg.modules.erp.utils.NumberUtil;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.erp.stock.entity.ErpGoodsStockLog;

/**
 * @Description: 商品库存表
 * @Author: nbacheng
 * @Date:   2022-11-25
 * @Version: V1.0
 */
@Service
public class ErpGoodsStockServiceImpl extends ServiceImpl<ErpGoodsStockMapper, ErpGoodsStock> implements IErpGoodsStockService {

	@Autowired
	private ErpGoodsStockMapper erpGoodsStockMapper;
	
	@Autowired
	private IErpGoodsStockLogService erpGoodsStockLogService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void inStock(ErpGoodsStockVo erpGoodsStockVo) throws Exception {
			Wrapper<ErpGoodsStock> queryWrapper = Wrappers.lambdaQuery(ErpGoodsStock.class)
			        .eq(ErpGoodsStock::getGoodsId, erpGoodsStockVo.getGoodsId()).eq(ErpGoodsStock::getScId, erpGoodsStockVo.getScId());
	
			ErpGoodsStock inerpGoodsStock =  erpGoodsStockMapper.selectOne(queryWrapper);
			ErpGoodsStockLog erpGoodsStockLog = new ErpGoodsStockLog();
			if(inerpGoodsStock == null) {
			//第一次入库，新增记录
			inerpGoodsStock = new ErpGoodsStock();
			inerpGoodsStock.setScId(erpGoodsStockVo.getScId());
			inerpGoodsStock.setGoodsId(erpGoodsStockVo.getGoodsId());
			inerpGoodsStock.setStockNum(erpGoodsStockVo.getStockNum());
			inerpGoodsStock.setTaxPrice(erpGoodsStockVo.getTaxPrice());
			inerpGoodsStock.setTaxAmount(erpGoodsStockVo.getTaxAmount());
			inerpGoodsStock.setUnTaxPrice(erpGoodsStockVo.getUnTaxPrice());
			inerpGoodsStock.setUnTaxAmount(erpGoodsStockVo.getUnTaxAmount());
	
			erpGoodsStockMapper.insert(inerpGoodsStock);
			
			erpGoodsStockLog.setOriStockNum(0);
			erpGoodsStockLog.setOriUnTaxPrice(BigDecimal.ZERO);
			erpGoodsStockLog.setOriTaxPrice(BigDecimal.ZERO);
		}
		else {//已有库存数量金额更新
			LambdaUpdateWrapper<ErpGoodsStock> updateWrapper = Wrappers.lambdaUpdate(ErpGoodsStock.class)
			        .eq(ErpGoodsStock::getScId, erpGoodsStockVo.getScId())
			        .eq(ErpGoodsStock::getGoodsId, erpGoodsStockVo.getGoodsId());
			
			erpGoodsStockLog.setOriStockNum(inerpGoodsStock.getStockNum());
			erpGoodsStockLog.setOriUnTaxPrice(inerpGoodsStock.getUnTaxPrice());
			erpGoodsStockLog.setOriTaxPrice(inerpGoodsStock.getTaxPrice());
			inerpGoodsStock.setScId(erpGoodsStockVo.getScId());
			inerpGoodsStock.setGoodsId(erpGoodsStockVo.getGoodsId());
			inerpGoodsStock.setStockNum(inerpGoodsStock.getStockNum() + erpGoodsStockVo.getStockNum());
			inerpGoodsStock.setTaxAmount(NumberUtil.add(inerpGoodsStock.getTaxAmount(), erpGoodsStockVo.getTaxAmount()));
			inerpGoodsStock.setTaxPrice(NumberUtil.div(inerpGoodsStock.getTaxAmount(), inerpGoodsStock.getStockNum()));
			inerpGoodsStock.setUnTaxAmount(NumberUtil.add(inerpGoodsStock.getUnTaxAmount(), erpGoodsStockVo.getUnTaxAmount()));
			inerpGoodsStock.setUnTaxPrice(NumberUtil.div(inerpGoodsStock.getUnTaxAmount(), inerpGoodsStock.getStockNum()));
			if(erpGoodsStockMapper.update(inerpGoodsStock, updateWrapper) != 1) {
				throw new Exception("入库更新信息已过期，请刷新重试！");
			}
		}
		erpGoodsStockLog.setGoodsId(erpGoodsStockVo.getGoodsId());
		erpGoodsStockLog.setScId(erpGoodsStockVo.getScId());
		erpGoodsStockLog.setStockNum(erpGoodsStockVo.getStockNum());
		erpGoodsStockLog.setTaxAmount(erpGoodsStockVo.getTaxAmount());
		erpGoodsStockLog.setUnTaxAmount(erpGoodsStockVo.getUnTaxAmount());
		erpGoodsStockLog.setCurStockNum(inerpGoodsStock.getStockNum());
		erpGoodsStockLog.setCurTaxPrice(inerpGoodsStock.getTaxPrice());
		erpGoodsStockLog.setCurUnTaxPrice(inerpGoodsStock.getUnTaxPrice());
		erpGoodsStockLog.setBizCode(erpGoodsStockVo.getBizCode());
		erpGoodsStockLog.setBizDetailId(erpGoodsStockVo.getBizDetailId());
		erpGoodsStockLog.setBizId(erpGoodsStockVo.getBizId());
		erpGoodsStockLog.setBizType(erpGoodsStockVo.getBizType());

		erpGoodsStockLogService.save(erpGoodsStockLog);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void outStock(ErpGoodsStockVo erpGoodsStockVo) throws Exception {
			Wrapper<ErpGoodsStock> queryWrapper = Wrappers.lambdaQuery(ErpGoodsStock.class)
			        .eq(ErpGoodsStock::getGoodsId, erpGoodsStockVo.getGoodsId()).eq(ErpGoodsStock::getScId, erpGoodsStockVo.getScId());
	
			ErpGoodsStock outerpGoodsStock =  erpGoodsStockMapper.selectOne(queryWrapper);
			ErpGoodsStockLog erpGoodsStockLog = new ErpGoodsStockLog();
			if(outerpGoodsStock == null) {
			//第一次出库，新增记录
			outerpGoodsStock = new ErpGoodsStock();
			outerpGoodsStock.setScId(erpGoodsStockVo.getScId());
			outerpGoodsStock.setGoodsId(erpGoodsStockVo.getGoodsId());
			outerpGoodsStock.setStockNum(0-erpGoodsStockVo.getStockNum());
			outerpGoodsStock.setTaxPrice(erpGoodsStockVo.getTaxPrice());
			outerpGoodsStock.setTaxAmount(NumberUtil.sub(BigDecimal.ZERO, erpGoodsStockVo.getTaxAmount()));
			outerpGoodsStock.setUnTaxPrice(erpGoodsStockVo.getUnTaxPrice());
			outerpGoodsStock.setUnTaxAmount(NumberUtil.sub(BigDecimal.ZERO, erpGoodsStockVo.getUnTaxAmount()));
	
			erpGoodsStockMapper.insert(outerpGoodsStock);
			
			erpGoodsStockLog.setOriStockNum(0);
			erpGoodsStockLog.setOriUnTaxPrice(BigDecimal.ZERO);
			erpGoodsStockLog.setOriTaxPrice(BigDecimal.ZERO);
		}
		else {//已有库存数量金额更新
			LambdaUpdateWrapper<ErpGoodsStock> updateWrapper = Wrappers.lambdaUpdate(ErpGoodsStock.class)
			        .eq(ErpGoodsStock::getScId, erpGoodsStockVo.getScId())
			        .eq(ErpGoodsStock::getGoodsId, erpGoodsStockVo.getGoodsId());
			
			erpGoodsStockLog.setOriStockNum(outerpGoodsStock.getStockNum());
			erpGoodsStockLog.setOriUnTaxPrice(outerpGoodsStock.getUnTaxPrice());
			erpGoodsStockLog.setOriTaxPrice(outerpGoodsStock.getTaxPrice());
			outerpGoodsStock.setScId(erpGoodsStockVo.getScId());
			outerpGoodsStock.setGoodsId(erpGoodsStockVo.getGoodsId());
			outerpGoodsStock.setStockNum(outerpGoodsStock.getStockNum() - erpGoodsStockVo.getStockNum());
			if(outerpGoodsStock.getStockNum() == 0) {
				outerpGoodsStock.setTaxPrice(NumberUtil.div(NumberUtil.add(erpGoodsStockVo.getTaxPrice(),outerpGoodsStock.getTaxPrice()),2));
				outerpGoodsStock.setUnTaxPrice(NumberUtil.div(NumberUtil.add(erpGoodsStockVo.getUnTaxPrice(),outerpGoodsStock.getUnTaxPrice()),2));
				outerpGoodsStock.setTaxAmount(BigDecimal.ZERO);
				outerpGoodsStock.setUnTaxAmount(BigDecimal.ZERO);
			}
			else {
				outerpGoodsStock.setTaxAmount(NumberUtil.sub(erpGoodsStockVo.getTaxAmount(),outerpGoodsStock.getTaxAmount()));
				outerpGoodsStock.setUnTaxAmount(NumberUtil.sub(erpGoodsStockVo.getUnTaxAmount(),outerpGoodsStock.getUnTaxAmount()));
				outerpGoodsStock.setTaxPrice(NumberUtil.div(outerpGoodsStock.getTaxAmount(), outerpGoodsStock.getStockNum()));
				outerpGoodsStock.setUnTaxPrice(NumberUtil.div(outerpGoodsStock.getUnTaxAmount(), outerpGoodsStock.getStockNum()));
			}
			
			if(erpGoodsStockMapper.update(outerpGoodsStock, updateWrapper) != 1) {
				throw new Exception("出库更新信息已过期，请刷新重试！");
			}
		}
		erpGoodsStockLog.setGoodsId(erpGoodsStockVo.getGoodsId());
		erpGoodsStockLog.setScId(erpGoodsStockVo.getScId());
		erpGoodsStockLog.setStockNum(erpGoodsStockVo.getStockNum());
		erpGoodsStockLog.setTaxAmount(erpGoodsStockVo.getTaxAmount());
		erpGoodsStockLog.setUnTaxAmount(erpGoodsStockVo.getUnTaxAmount());
		erpGoodsStockLog.setCurStockNum(outerpGoodsStock.getStockNum());
		erpGoodsStockLog.setStockNum(outerpGoodsStock.getStockNum());
		erpGoodsStockLog.setCurTaxPrice(outerpGoodsStock.getTaxPrice());
		erpGoodsStockLog.setCurUnTaxPrice(outerpGoodsStock.getUnTaxPrice());
		erpGoodsStockLog.setBizCode(erpGoodsStockVo.getBizCode());
		erpGoodsStockLog.setBizDetailId(erpGoodsStockVo.getBizDetailId());
		erpGoodsStockLog.setBizId(erpGoodsStockVo.getBizId());
		erpGoodsStockLog.setBizType(erpGoodsStockVo.getBizType());
	
		erpGoodsStockLogService.save(erpGoodsStockLog);
	}

}
