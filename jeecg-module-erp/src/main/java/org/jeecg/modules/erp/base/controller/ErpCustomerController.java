package org.jeecg.modules.erp.base.controller;

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
import org.jeecg.modules.erp.base.entity.ErpCustomer;
import org.jeecg.modules.erp.base.service.IErpCustomerService;

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
 * @Description: erp_customer
 * @Author: nbacheng
 * @Date:   2022-08-26
 * @Version: V1.0
 */
@Api(tags="erp_customer")
@RestController
@RequestMapping("/base/erpCustomer")
@Slf4j
public class ErpCustomerController extends JeecgController<ErpCustomer, IErpCustomerService> {
	@Autowired
	private IErpCustomerService erpCustomerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param erpCustomer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "erp_customer-分页列表查询")
	@ApiOperation(value="erp_customer-分页列表查询", notes="erp_customer-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ErpCustomer erpCustomer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ErpCustomer> queryWrapper = QueryGenerator.initQueryWrapper(erpCustomer, req.getParameterMap());
		Page<ErpCustomer> page = new Page<ErpCustomer>(pageNo, pageSize);
		IPage<ErpCustomer> pageList = erpCustomerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param erpCustomer
	 * @return
	 */
	@AutoLog(value = "erp_customer-添加")
	@ApiOperation(value="erp_customer-添加", notes="erp_customer-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ErpCustomer erpCustomer) {
		erpCustomerService.save(erpCustomer);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param erpCustomer
	 * @return
	 */
	@AutoLog(value = "erp_customer-编辑")
	@ApiOperation(value="erp_customer-编辑", notes="erp_customer-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ErpCustomer erpCustomer) {
		erpCustomerService.updateById(erpCustomer);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_customer-通过id删除")
	@ApiOperation(value="erp_customer-通过id删除", notes="erp_customer-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		erpCustomerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "erp_customer-批量删除")
	@ApiOperation(value="erp_customer-批量删除", notes="erp_customer-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.erpCustomerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "erp_customer-通过id查询")
	@ApiOperation(value="erp_customer-通过id查询", notes="erp_customer-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ErpCustomer erpCustomer = erpCustomerService.getById(id);
		if(erpCustomer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(erpCustomer);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param erpCustomer
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ErpCustomer erpCustomer) {
        return super.exportXls(request, erpCustomer, ErpCustomer.class, "erp_customer");
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
        return super.importExcel(request, response, ErpCustomer.class);
    }

}
