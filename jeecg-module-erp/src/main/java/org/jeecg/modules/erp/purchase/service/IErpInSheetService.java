package org.jeecg.modules.erp.purchase.service;

import org.jeecg.modules.erp.purchase.entity.ErpInSheetDetail;
import org.jeecg.modules.erp.purchase.entity.ErpInSheet;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.jeecg.common.api.vo.Result;

/**
 * @Description: erp_in_sheet
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
public interface IErpInSheetService extends IService<ErpInSheet> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ErpInSheet erpInSheet,List<ErpInSheetDetail> erpInSheetDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ErpInSheet erpInSheet,List<ErpInSheetDetail> erpInSheetDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
	/**
	 * 
	 * @param id
	 * @return 
	 * @throws Exception 
	 */
	public Result approvePass (String id) throws Exception;
	
}
