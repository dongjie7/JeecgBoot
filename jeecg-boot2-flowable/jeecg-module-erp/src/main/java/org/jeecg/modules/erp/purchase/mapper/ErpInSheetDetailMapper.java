package org.jeecg.modules.erp.purchase.mapper;

import java.util.List;
import org.jeecg.modules.erp.purchase.entity.ErpInSheetDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: erp_in_sheet_detail
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
public interface ErpInSheetDetailMapper extends BaseMapper<ErpInSheetDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ErpInSheetDetail> selectByMainId(@Param("mainId") String mainId);
}
