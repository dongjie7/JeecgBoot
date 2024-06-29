package org.jeecg.modules.erp.sale.service.impl;

import org.jeecg.modules.erp.apithird.entity.SysUser;
import org.jeecg.modules.erp.apithird.service.IErpThirdService;
import org.jeecg.modules.erp.enums.GoodsStockType;
import org.jeecg.modules.erp.sale.entity.ErpSaleOut;
import org.jeecg.modules.erp.sale.entity.ErpSaleOutDetail;
import org.jeecg.modules.erp.sale.mapper.ErpSaleOutDetailMapper;
import org.jeecg.modules.erp.sale.mapper.ErpSaleOutMapper;
import org.jeecg.modules.erp.sale.service.IErpSaleOutDetailService;
import org.jeecg.modules.erp.sale.service.IErpSaleOutService;
import org.jeecg.modules.erp.stock.service.IErpGoodsStockService;
import org.jeecg.modules.erp.stock.vo.ErpGoodsStockVo;
import org.jeecg.modules.erp.utils.NumberUtil;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import java.util.Collection;

/**
 * @Description: 销售出库单
 * @Author: nbacheng
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Service
public class ErpSaleOutServiceImpl extends ServiceImpl<ErpSaleOutMapper, ErpSaleOut> implements IErpSaleOutService {

	@Autowired
	private ErpSaleOutMapper erpSaleOutMapper;
	@Autowired
	private ErpSaleOutDetailMapper erpSaleOutDetailMapper;
	@Autowired
	private IErpSaleOutDetailService erpSaleOutDetailService;
	@Resource
    private IErpThirdService iErpThirdService;
	@Autowired
	private IErpGoodsStockService erpGoodsStockService;
	
	@Override
	@Transactional
	public void saveMain(ErpSaleOut erpSaleOut, List<ErpSaleOutDetail> erpSaleOutDetailList) {
		erpSaleOutMapper.insert(erpSaleOut);
		if(erpSaleOutDetailList!=null && erpSaleOutDetailList.size()>0) {
			int orderNo = 1;
			for(ErpSaleOutDetail entity:erpSaleOutDetailList) {
				//外键设置
				entity.setSheetId(erpSaleOut.getId());
				//排序编号设置
				entity.setOrderNo(orderNo);
				erpSaleOutDetailMapper.insert(entity);
				orderNo++;
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ErpSaleOut erpSaleOut,List<ErpSaleOutDetail> erpSaleOutDetailList) {
		erpSaleOutMapper.updateById(erpSaleOut);
		
		//1.先删除子表数据
		erpSaleOutDetailMapper.deleteByMainId(erpSaleOut.getId());
		
		//2.子表数据重新插入
		if(erpSaleOutDetailList!=null && erpSaleOutDetailList.size()>0) {
			int orderNo = 1;
			for(ErpSaleOutDetail entity:erpSaleOutDetailList) {
				//外键设置
				entity.setSheetId(erpSaleOut.getId());
				//排序编号设置
				entity.setOrderNo(orderNo);
				erpSaleOutDetailMapper.insert(entity);
				orderNo++;
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		erpSaleOutDetailMapper.deleteByMainId(id);
		erpSaleOutMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			erpSaleOutDetailMapper.deleteByMainId(id.toString());
			erpSaleOutMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<?> approvePass(String id) throws Exception {
		ErpSaleOut erpSaleOut = erpSaleOutMapper.selectById(id);
		if (erpSaleOut == null) {
			return Result.error("销售出库单不存在！");
		}
		if (erpSaleOut.getStatus() == 2 ) {
			return Result.error("销售出库单已审核通过，不能再次审核！");
		}
		SysUser loginUser = iErpThirdService.getLoginUser();
		LambdaUpdateWrapper<ErpSaleOut> updateOrderWrapper = Wrappers.lambdaUpdate(ErpSaleOut.class)
		        .set(ErpSaleOut::getApproveBy, loginUser.getUsername())
		        .set(ErpSaleOut::getApproveTime, LocalDateTime.now())
		        .eq(ErpSaleOut::getId, erpSaleOut.getId());
		erpSaleOut.setStatus(2); //审核通过标志 2代表通过    
		if(erpSaleOutMapper.update(erpSaleOut, updateOrderWrapper) != 1) {
			return Result.error("销售出库单信息已过期，请刷新重试！");
		}
		Wrapper<ErpSaleOutDetail> queryDetailWrapper = Wrappers.lambdaQuery(ErpSaleOutDetail.class)
		        .eq(ErpSaleOutDetail::getSheetId, erpSaleOut.getId())
		        .orderByAsc(ErpSaleOutDetail::getOrderNo);
		    List<ErpSaleOutDetail> details = erpSaleOutDetailService.list(queryDetailWrapper);
		    for (ErpSaleOutDetail detail : details) {
		    	ErpGoodsStockVo erpGoodsStockVo = new ErpGoodsStockVo();
		    	erpGoodsStockVo.setScId(erpSaleOut.getScId());
		    	erpGoodsStockVo.setGoodsId(detail.getGoodsId());
		    	erpGoodsStockVo.setStockNum(detail.getOrderNum());
		    	erpGoodsStockVo.setTaxAmount(NumberUtil.mul(detail.getTaxPrice(), detail.getOrderNum()));
		    	erpGoodsStockVo.setUnTaxAmount(NumberUtil.mul(NumberUtil.calcUnTaxPrice(detail.getTaxPrice(), detail.getTaxRate()), detail.getOrderNum()));
		    	erpGoodsStockVo.setTaxPrice(detail.getTaxPrice());
		    	erpGoodsStockVo.setUnTaxPrice(NumberUtil.calcUnTaxPrice(detail.getTaxPrice(), detail.getTaxRate()));
		    	erpGoodsStockVo.setBizCode(erpSaleOut.getCode());
		    	erpGoodsStockVo.setBizDetailId(detail.getId());
		    	erpGoodsStockVo.setBizId(erpSaleOut.getId());
		    	erpGoodsStockVo.setBizType(GoodsStockType.SALE.getCode());

		      erpGoodsStockService.outStock(erpGoodsStockVo);
		    }
		return Result.OK("审核通过完成");
	}
	
}
