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
import org.jeecg.modules.erp.goods.entity.ErpGoodsProperty;
import org.jeecg.modules.erp.goods.service.IErpGoodsPropertyService;

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
 * @Description: erp_goods_property
 * @Author: nbacheng
 * @Date:   2022-08-29
 * @Version: V1.0
 */
@Api(tags="erp_goods_property")
@RestController
@RequestMapping("/goods/erpGoodsProperty")
@Slf4j
public class ErpGoodsPropertyController extends JeecgController<ErpGoodsProperty, IErpGoodsPropertyService> {
	@Autowired
	private IErpGoodsPropertyService erpGoodsPropertyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpGoodsProperty
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "erp_goods_property-分页列表查询")
	@ApiOperation(value="erp_goods_property-分页列表查询", notes="erp_goods_property-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ErpGoodsProperty erpGoodsProperty,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ErpGoodsProperty> queryWrapper = QueryGenerator.initQueryWrapper(erpGoodsProperty, req.getParameterMap());
		Page<ErpGoodsProperty> page = new Page<ErpGoodsProperty>(pageNo, pageSize);
		IPage<ErpGoodsProperty> pageList = erpGoodsPropertyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param erpGoodsProperty
	 * @return
	 */
	@AutoLog(value = "erp_goods_property-添加")
	@ApiOperation(value="erp_goods_property-添加", notes="erp_goods_property-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpGoodsProperty erpGoodsProperty) {
		erpGoodsPropertyService.save(erpGoodsProperty);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param erpGoodsProperty
	 * @return
	 */
	@AutoLog(value = "erp_goods_property-编辑")
	@ApiOperation(value="erp_goods_property-编辑", notes="erp_goods_property-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpGoodsProperty erpGoodsProperty) {
		erpGoodsPropertyService.updateById(erpGoodsProperty);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_goods_property-通过id删除")
	@ApiOperation(value="erp_goods_property-通过id删除", notes="erp_goods_property-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpGoodsPropertyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "erp_goods_property-批量删除")
	@ApiOperation(value="erp_goods_property-批量删除", notes="erp_goods_property-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpGoodsPropertyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_goods_property-通过id查询")
	@ApiOperation(value="erp_goods_property-通过id查询", notes="erp_goods_property-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpGoodsProperty erpGoodsProperty = erpGoodsPropertyService.getById(id);
		if(erpGoodsProperty==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpGoodsProperty);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpGoodsProperty
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpGoodsProperty erpGoodsProperty) {
        return super.exportXls(request, erpGoodsProperty, ErpGoodsProperty.class, "erp_goods_property");
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
        return super.importExcel(request, response, ErpGoodsProperty.class);
    }

}
