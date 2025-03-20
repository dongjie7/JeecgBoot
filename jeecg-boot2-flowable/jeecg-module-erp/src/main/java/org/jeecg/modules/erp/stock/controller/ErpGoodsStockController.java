package org.jeecg.modules.erp.stock.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.erp.stock.entity.ErpGoodsStock;
import org.jeecg.modules.erp.stock.service.IErpGoodsStockService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 商品库存表
 * @Author: nbacheng
 * @Date:   2022-11-25
 * @Version: V1.0
 */
@Api(tags="商品库存表")
@RestController
@RequestMapping("/stock/erpGoodsStock")
@Slf4j
public class ErpGoodsStockController extends JeecgController<ErpGoodsStock, IErpGoodsStockService> {
	@Autowired
	private IErpGoodsStockService erpGoodsStockService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpGoodsStock
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "商品库存表-分页列表查询")
	@ApiOperation(value="商品库存表-分页列表查询", notes="商品库存表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ErpGoodsStock erpGoodsStock,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ErpGoodsStock> queryWrapper = QueryGenerator.initQueryWrapper(erpGoodsStock, req.getParameterMap());
		Page<ErpGoodsStock> page = new Page<ErpGoodsStock>(pageNo, pageSize);
		IPage<ErpGoodsStock> pageList = erpGoodsStockService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param erpGoodsStock
	 * @return
	 */
	@AutoLog(value = "商品库存表-添加")
	@ApiOperation(value="商品库存表-添加", notes="商品库存表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpGoodsStock erpGoodsStock) {
		erpGoodsStockService.save(erpGoodsStock);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param erpGoodsStock
	 * @return
	 */
	@AutoLog(value = "商品库存表-编辑")
	@ApiOperation(value="商品库存表-编辑", notes="商品库存表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpGoodsStock erpGoodsStock) {
		erpGoodsStockService.updateById(erpGoodsStock);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品库存表-通过id删除")
	@ApiOperation(value="商品库存表-通过id删除", notes="商品库存表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpGoodsStockService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商品库存表-批量删除")
	@ApiOperation(value="商品库存表-批量删除", notes="商品库存表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpGoodsStockService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品库存表-通过id查询")
	@ApiOperation(value="商品库存表-通过id查询", notes="商品库存表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpGoodsStock erpGoodsStock = erpGoodsStockService.getById(id);
		if(erpGoodsStock==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpGoodsStock);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpGoodsStock
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpGoodsStock erpGoodsStock) {
        return super.exportXls(request, erpGoodsStock, ErpGoodsStock.class, "商品库存表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ErpGoodsStock.class);
    }

}
