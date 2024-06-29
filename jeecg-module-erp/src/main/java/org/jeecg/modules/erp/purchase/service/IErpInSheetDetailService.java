package org.jeecg.modules.erp.purchase.service;

import org.jeecg.modules.erp.purchase.entity.ErpInSheetDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: erp_in_sheet_detail
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
public interface IErpInSheetDetailService extends IService<ErpInSheetDetail> {

	public List<ErpInSheetDetail> selectByMainId(String mainId);
}
