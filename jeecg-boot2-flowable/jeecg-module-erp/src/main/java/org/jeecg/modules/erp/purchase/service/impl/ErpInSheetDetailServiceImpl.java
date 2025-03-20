package org.jeecg.modules.erp.purchase.service.impl;

import org.jeecg.modules.erp.purchase.entity.ErpInSheetDetail;
import org.jeecg.modules.erp.purchase.mapper.ErpInSheetDetailMapper;
import org.jeecg.modules.erp.purchase.service.IErpInSheetDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: erp_in_sheet_detail
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
@Service
public class ErpInSheetDetailServiceImpl extends ServiceImpl<ErpInSheetDetailMapper, ErpInSheetDetail> implements IErpInSheetDetailService {
	
	@Autowired
	private ErpInSheetDetailMapper erpInSheetDetailMapper;
	
	@Override
	public List<ErpInSheetDetail> selectByMainId(String mainId) {
		return erpInSheetDetailMapper.selectByMainId(mainId);
	}
}
