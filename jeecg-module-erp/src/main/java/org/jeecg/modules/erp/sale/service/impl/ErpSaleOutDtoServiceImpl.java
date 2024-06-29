package org.jeecg.modules.erp.sale.service.impl;

import org.jeecg.modules.erp.sale.dto.ErpSaleOutDto;
import org.jeecg.modules.erp.sale.mapper.ErpSaleOutDtoMapper;
import org.jeecg.modules.erp.sale.service.IErpSaleOutDtoService;
import org.jeecg.modules.erp.sale.vo.QuerySaleOutVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: erp_goods_dto
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Service
public class ErpSaleOutDtoServiceImpl extends ServiceImpl<ErpSaleOutDtoMapper, ErpSaleOutDto> implements IErpSaleOutDtoService {

	@Autowired
	private ErpSaleOutDtoMapper erpSaleOutDtoMapper;
	
	@Override
	public IPage<ErpSaleOutDto> querySaleOutList(Page<ErpSaleOutDto> page, QuerySaleOutVo querySaleOutVo) {
		List<ErpSaleOutDto> erpSaleOutDtoLists = this.baseMapper.querySaleOutList(page, querySaleOutVo);
        return page.setRecords(erpSaleOutDtoLists);
	}

}
