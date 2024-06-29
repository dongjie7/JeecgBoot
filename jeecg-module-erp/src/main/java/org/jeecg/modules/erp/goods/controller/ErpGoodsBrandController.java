package org.jeecg.modules.erp.goods.controller;

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
import org.jeecg.modules.erp.goods.entity.ErpGoodsBrand;
import org.jeecg.modules.erp.goods.service.IErpGoodsBrandService;

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
 * @Description: erp_goods_brand
 * @Author: nbacheng
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Api(tags="erp_goods_brand")
@RestController
@RequestMapping("/goods/erpGoodsBrand")
@Slf4j
public class ErpGoodsBrandController extends JeecgController<ErpGoodsBrand, IErpGoodsBrandService> {
	@Autowired
	private IErpGoodsBrandService erpGoodsBrandService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpGoodsBrand
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "erp_goods_brand-分页列表查询")
	@ApiOperation(value="erp_goods_brand-分页列表查询", notes="erp_goods_brand-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ErpGoodsBrand erpGoodsBrand,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ErpGoodsBrand> queryWrapper = QueryGenerator.initQueryWrapper(erpGoodsBrand, req.getParameterMap());
		Page<ErpGoodsBrand> page = new Page<ErpGoodsBrand>(pageNo, pageSize);
		IPage<ErpGoodsBrand> pageList = erpGoodsBrandService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param erpGoodsBrand
	 * @return
	 */
	@AutoLog(value = "erp_goods_brand-添加")
	@ApiOperation(value="erp_goods_brand-添加", notes="erp_goods_brand-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpGoodsBrand erpGoodsBrand) {
		erpGoodsBrandService.save(erpGoodsBrand);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param erpGoodsBrand
	 * @return
	 */
	@AutoLog(value = "erp_goods_brand-编辑")
	@ApiOperation(value="erp_goods_brand-编辑", notes="erp_goods_brand-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpGoodsBrand erpGoodsBrand) {
		erpGoodsBrandService.updateById(erpGoodsBrand);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_goods_brand-通过id删除")
	@ApiOperation(value="erp_goods_brand-通过id删除", notes="erp_goods_brand-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpGoodsBrandService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "erp_goods_brand-批量删除")
	@ApiOperation(value="erp_goods_brand-批量删除", notes="erp_goods_brand-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpGoodsBrandService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_goods_brand-通过id查询")
	@ApiOperation(value="erp_goods_brand-通过id查询", notes="erp_goods_brand-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpGoodsBrand erpGoodsBrand = erpGoodsBrandService.getById(id);
		if(erpGoodsBrand==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpGoodsBrand);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpGoodsBrand
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpGoodsBrand erpGoodsBrand) {
        return super.exportXls(request, erpGoodsBrand, ErpGoodsBrand.class, "erp_goods_brand");
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
        return super.importExcel(request, response, ErpGoodsBrand.class);
    }

}
