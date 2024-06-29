package org.jeecg.modules.erp.purchase.controller;

import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;

import org.jeecg.modules.erp.purchase.dto.ErpInSheetDto;
import org.jeecg.modules.erp.purchase.service.IErpInSheetDtoService;
import org.jeecg.modules.erp.purchase.vo.QueryInSheetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 销售入库单DTO
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Api(tags="销售入库单DTO")
@RestController
@RequestMapping("/purchase/erpInSheetDto")
@Slf4j
public class ErpInSheetDtoController {
	@Autowired
	private IErpInSheetDtoService erpInSheetDtoService;

	
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
	public Result<?> queryInSheetList(QueryInSheetVo queryInSheetVo,
			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			   HttpServletRequest req) {
      Page<ErpInSheetDto> page = new Page<ErpInSheetDto>(pageNo, pageSize);
      IPage<ErpInSheetDto> pageList = erpInSheetDtoService.queryInSheetList(page, queryInSheetVo);
      return Result.OK(pageList);
    }
	
}
