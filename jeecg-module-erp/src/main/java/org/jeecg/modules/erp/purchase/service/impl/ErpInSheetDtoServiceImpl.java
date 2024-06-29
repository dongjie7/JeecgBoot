package org.jeecg.modules.erp.purchase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.erp.purchase.dto.ErpInSheetDto;
import org.jeecg.modules.erp.purchase.mapper.ErpInSheetDtoMapper;
import org.jeecg.modules.erp.purchase.service.IErpInSheetDtoService;
import org.jeecg.modules.erp.purchase.vo.QueryInSheetVo;

/**
 * @Description: erp_goods_dto
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Service
public class ErpInSheetDtoServiceImpl extends ServiceImpl<ErpInSheetDtoMapper, ErpInSheetDto> implements IErpInSheetDtoService {

	@Autowired
	private ErpInSheetDtoMapper erpInSheetDtoMapper;
	
	@Override
	public IPage<ErpInSheetDto> queryInSheetList(Page<ErpInSheetDto> page, QueryInSheetVo queryInSheetVo) {
		List<ErpInSheetDto> erpInSheetDtoLists = this.baseMapper.queryInSheetList(page, queryInSheetVo);
        return page.setRecords(erpInSheetDtoLists);
	}

}
