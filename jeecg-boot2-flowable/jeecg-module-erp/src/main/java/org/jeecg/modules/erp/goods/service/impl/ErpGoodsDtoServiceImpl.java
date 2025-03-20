package org.jeecg.modules.erp.goods.service.impl;

import org.jeecg.modules.erp.goods.dto.ErpGoodsDto;
import org.jeecg.modules.erp.goods.mapper.ErpGoodsDtoMapper;
import org.jeecg.modules.erp.goods.service.IErpGoodsDtoService;
import org.jeecg.modules.erp.goods.vo.QueryGoodsVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: erp_goods
 * @Author: nbacheng
 * @Date:   2023-02-09
 * @Version: V1.0
 */
@Service
public class ErpGoodsDtoServiceImpl extends ServiceImpl<ErpGoodsDtoMapper, ErpGoodsDto> implements IErpGoodsDtoService {

	@Autowired
	private ErpGoodsDtoMapper erpGoodsDtoMapper;
	
	@Override
	public IPage<ErpGoodsDto> queryGoodsList(Page<ErpGoodsDto> page, QueryGoodsVo queryGoodsVo) {
		List<ErpGoodsDto> erpGoodsDtoLists = this.baseMapper.queryGoodsList(page, queryGoodsVo);
        return page.setRecords(erpGoodsDtoLists);
	}

	@Override
	public List<ErpGoodsDto> queryByIds(String ids) {
		// TODO Auto-generated method stub
		String [] idArray=ids.split(",");
		return erpGoodsDtoMapper.getByIds(idArray);
	}

}
