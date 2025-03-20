package org.jeecg.modules.erp.sale.controller;

import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.erp.sale.dto.ErpSaleOutDto;
import org.jeecg.modules.erp.sale.vo.QuerySaleOutVo;
import org.jeecg.modules.erp.sale.service.IErpSaleOutDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 销售出库单DTO
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Api(tags="销售出库单DTO")
@RestController
@RequestMapping("/sale/erpSaleOutDto")
@Slf4j
public class ErpSaleOutDtoController {
	@Autowired
	private IErpSaleOutDtoService erpSaleOutDtoService;

	
	/**
	 * 分页列表查询
	 *
	 * @param erpSaleOut
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "销售出库单-分页列表查询")
	@ApiOperation(value="销售出库单-分页列表查询", notes="销售出库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> querySaleOutList(QuerySaleOutVo querySaleOutVo,
			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			   HttpServletRequest req) {
      Page<ErpSaleOutDto> page = new Page<ErpSaleOutDto>(pageNo, pageSize);
      IPage<ErpSaleOutDto> pageList = erpSaleOutDtoService.querySaleOutList(page, querySaleOutVo);
      return Result.OK(pageList);
    }
	
}
