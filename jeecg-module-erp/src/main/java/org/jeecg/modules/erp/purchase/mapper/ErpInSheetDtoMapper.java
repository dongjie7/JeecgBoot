package org.jeecg.modules.erp.purchase.mapper;

import org.jeecg.modules.erp.purchase.dto.ErpInSheetDto;
import org.jeecg.modules.erp.purchase.vo.QueryInSheetVo;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 销售出库单
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */
public interface ErpInSheetDtoMapper extends BaseMapper<ErpInSheetDto> {
	List<ErpInSheetDto> queryInSheetList(Page<ErpInSheetDto> page, @Param("vo") QueryInSheetVo vo);
}
