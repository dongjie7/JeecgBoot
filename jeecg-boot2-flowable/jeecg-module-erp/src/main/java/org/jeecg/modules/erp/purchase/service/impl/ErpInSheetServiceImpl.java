package org.jeecg.modules.erp.purchase.service.impl;

import org.jeecg.modules.erp.apithird.entity.SysUser;
import org.jeecg.modules.erp.apithird.service.IErpThirdService;
import org.jeecg.modules.erp.enums.GoodsStockType;
import org.jeecg.modules.erp.purchase.entity.ErpInSheet;
import org.jeecg.modules.erp.purchase.entity.ErpInSheetDetail;
import org.jeecg.modules.erp.purchase.mapper.ErpInSheetDetailMapper;
import org.jeecg.modules.erp.purchase.mapper.ErpInSheetMapper;
import org.jeecg.modules.erp.purchase.service.IErpInSheetDetailService;
import org.jeecg.modules.erp.purchase.service.IErpInSheetService;
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
 * @Description: erp_in_sheet
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
@Service
public class ErpInSheetServiceImpl extends ServiceImpl<ErpInSheetMapper, ErpInSheet> implements IErpInSheetService {

	@Autowired
	private ErpInSheetMapper erpInSheetMapper;
	
	@Autowired
	private ErpInSheetDetailMapper erpInSheetDetailMapper;
	
	@Autowired
	private IErpInSheetDetailService erpInSheetDetailService;
	
	@Resource
    private IErpThirdService iErpThirdService;
	
	@Autowired
	private IErpGoodsStockService erpGoodsStockService;
	
	@Override
	@Transactional
	public void saveMain(ErpInSheet erpInSheet, List<ErpInSheetDetail> erpInSheetDetailList) {
		erpInSheetMapper.insert(erpInSheet);
		if(erpInSheetDetailList!=null && erpInSheetDetailList.size()>0) {
			int orderNo = 1;
			for(ErpInSheetDetail entity:erpInSheetDetailList) {
				//外键设置
				entity.setSheetId(erpInSheet.getId());
				//排序编号设置
				entity.setOrderNo(orderNo);
				erpInSheetDetailMapper.insert(entity);
				orderNo++;
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ErpInSheet erpInSheet,List<ErpInSheetDetail> erpInSheetDetailList) {
		erpInSheetMapper.updateById(erpInSheet);
		
		//1.先删除子表数据
		erpInSheetDetailMapper.deleteByMainId(erpInSheet.getId());
		
		//2.子表数据重新插入
		if(erpInSheetDetailList!=null && erpInSheetDetailList.size()>0) {
			int orderNo = 1;
			for(ErpInSheetDetail entity:erpInSheetDetailList) {
				//外键设置
				entity.setSheetId(erpInSheet.getId());
				//排序编号设置
				entity.setOrderNo(orderNo);
				erpInSheetDetailMapper.insert(entity);
				orderNo++;
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		erpInSheetDetailMapper.deleteByMainId(id);
		erpInSheetMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			erpInSheetDetailMapper.deleteByMainId(id.toString());
			erpInSheetMapper.deleteById(id);
		}
	}

	/**
	 * 
	 * @param id
	 * @return 
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result approvePass(String id) throws Exception {
		ErpInSheet erpInSheet = erpInSheetMapper.selectById(id);
		if (erpInSheet == null) {
			return Result.error("采购入库单不存在！");
		}
		if (erpInSheet.getStatus() == 2 ) {
			return Result.error("采购入库单已审核通过，不能再次审核！");
		}
		
		SysUser loginUser = iErpThirdService.getLoginUser();
		LambdaUpdateWrapper<ErpInSheet> updateOrderWrapper = Wrappers.lambdaUpdate(ErpInSheet.class)
		        .set(ErpInSheet::getApproveBy, loginUser.getUsername())
		        .set(ErpInSheet::getApproveTime, LocalDateTime.now())
		        .eq(ErpInSheet::getId, erpInSheet.getId());
		erpInSheet.setStatus(2); //审核通过标志 2代表通过    
		if(erpInSheetMapper.update(erpInSheet, updateOrderWrapper) != 1) {
			return Result.error("采购入库单信息已过期，请刷新重试！");
		}
		Wrapper<ErpInSheetDetail> queryDetailWrapper = Wrappers.lambdaQuery(ErpInSheetDetail.class)
		        .eq(ErpInSheetDetail::getSheetId, erpInSheet.getId())
		        .orderByAsc(ErpInSheetDetail::getOrderNo);
		    List<ErpInSheetDetail> details = erpInSheetDetailService.list(queryDetailWrapper);
		    for (ErpInSheetDetail detail : details) {
		    	ErpGoodsStockVo erpGoodsStockVo = new ErpGoodsStockVo();
		    	erpGoodsStockVo.setScId(erpInSheet.getScId());
		    	erpGoodsStockVo.setGoodsId(detail.getGoodsId());
		    	erpGoodsStockVo.setStockNum(detail.getOrderNum());
		    	erpGoodsStockVo.setTaxAmount(NumberUtil.mul(detail.getTaxPrice(), detail.getOrderNum()));
		    	erpGoodsStockVo.setUnTaxAmount(NumberUtil.mul(NumberUtil.calcUnTaxPrice(detail.getTaxPrice(), detail.getTaxRate()), detail.getOrderNum()));
		    	erpGoodsStockVo.setTaxPrice(detail.getTaxPrice());
		    	erpGoodsStockVo.setUnTaxPrice(NumberUtil.calcUnTaxPrice(detail.getTaxPrice(), detail.getTaxRate()));
		    	erpGoodsStockVo.setBizCode(erpInSheet.getCode());
		    	erpGoodsStockVo.setBizDetailId(detail.getId());
		    	erpGoodsStockVo.setBizId(erpInSheet.getId());
		    	erpGoodsStockVo.setBizType(GoodsStockType.PURCHASE.getCode());

		      erpGoodsStockService.inStock(erpGoodsStockVo);
		    }
		return Result.OK("审核通过完成");
	}
	
}
