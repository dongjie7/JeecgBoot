package org.jeecg.modules.erp.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.erp.sale.dto.ErpSaleOutDto;
import org.jeecg.modules.erp.sale.vo.QuerySaleOutVo;

/**
 * @Description: 销售出库单
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */
public interface IErpSaleOutDtoService extends IService<ErpSaleOutDto> {

	IPage<ErpSaleOutDto> querySaleOutList(Page<ErpSaleOutDto> page, QuerySaleOutVo querySaleOutVo);

}
