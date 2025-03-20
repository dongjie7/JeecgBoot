package org.jeecg.modules.erp.sale.mapper;

import org.jeecg.modules.erp.sale.dto.ErpSaleOutDto;
import org.jeecg.modules.erp.sale.vo.QuerySaleOutVo;
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
public interface ErpSaleOutDtoMapper extends BaseMapper<ErpSaleOutDto> {
	List<ErpSaleOutDto> querySaleOutList(Page<ErpSaleOutDto> page, @Param("vo") QuerySaleOutVo vo);
}
