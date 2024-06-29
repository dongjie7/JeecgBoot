package org.jeecg.modules.estar.bs.controller;

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
import org.jeecg.modules.estar.bs.entity.BsDataSetParam;
import org.jeecg.modules.estar.bs.service.IBsDataSetParamService;

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
 * @Description: bs_data_set_param
 * @Author: nbacheng
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Api(tags="bs_data_set_param")
@RestController
@RequestMapping("/bs/bsDataSetParam")
@Slf4j
public class BsDataSetParamController extends JeecgController<BsDataSetParam, IBsDataSetParamService> {
	@Autowired
	private IBsDataSetParamService bsDataSetParamService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsDataSetParam
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "bs_data_set_param-分页列表查询")
	@ApiOperation(value="bs_data_set_param-分页列表查询", notes="bs_data_set_param-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsDataSetParam bsDataSetParam,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsDataSetParam> queryWrapper = QueryGenerator.initQueryWrapper(bsDataSetParam, req.getParameterMap());
		Page<BsDataSetParam> page = new Page<BsDataSetParam>(pageNo, pageSize);
		IPage<BsDataSetParam> pageList = bsDataSetParamService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsDataSetParam
	 * @return
	 */
	@AutoLog(value = "bs_data_set_param-添加")
	@ApiOperation(value="bs_data_set_param-添加", notes="bs_data_set_param-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsDataSetParam bsDataSetParam) {
		bsDataSetParamService.save(bsDataSetParam);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsDataSetParam
	 * @return
	 */
	@AutoLog(value = "bs_data_set_param-编辑")
	@ApiOperation(value="bs_data_set_param-编辑", notes="bs_data_set_param-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsDataSetParam bsDataSetParam) {
		bsDataSetParamService.updateById(bsDataSetParam);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_data_set_param-通过id删除")
	@ApiOperation(value="bs_data_set_param-通过id删除", notes="bs_data_set_param-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsDataSetParamService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bs_data_set_param-批量删除")
	@ApiOperation(value="bs_data_set_param-批量删除", notes="bs_data_set_param-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsDataSetParamService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bs_data_set_param-通过id查询")
	@ApiOperation(value="bs_data_set_param-通过id查询", notes="bs_data_set_param-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsDataSetParam bsDataSetParam = bsDataSetParamService.getById(id);
		if(bsDataSetParam==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsDataSetParam);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsDataSetParam
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsDataSetParam bsDataSetParam) {
        return super.exportXls(request, bsDataSetParam, BsDataSetParam.class, "bs_data_set_param");
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
        return super.importExcel(request, response, BsDataSetParam.class);
    }

}
