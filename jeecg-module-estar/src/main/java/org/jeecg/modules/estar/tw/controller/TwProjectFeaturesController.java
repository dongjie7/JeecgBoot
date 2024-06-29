package org.jeecg.modules.estar.tw.controller;

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
import org.jeecg.modules.estar.tw.entity.TwProjectFeatures;
import org.jeecg.modules.estar.tw.service.ITwProjectFeaturesService;

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
 * @Description: 版本库表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Api(tags="版本库表")
@RestController
@RequestMapping("/tw/twProjectFeatures")
@Slf4j
public class TwProjectFeaturesController extends JeecgController<TwProjectFeatures, ITwProjectFeaturesService> {
	@Autowired
	private ITwProjectFeaturesService twProjectFeaturesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param twProjectFeatures
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "版本库表-分页列表查询")
	@ApiOperation(value="版本库表-分页列表查询", notes="版本库表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TwProjectFeatures twProjectFeatures,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TwProjectFeatures> queryWrapper = QueryGenerator.initQueryWrapper(twProjectFeatures, req.getParameterMap());
		Page<TwProjectFeatures> page = new Page<TwProjectFeatures>(pageNo, pageSize);
		IPage<TwProjectFeatures> pageList = twProjectFeaturesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   列表
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "版本库表-列表")
	@ApiOperation(value="版本库表-列表", notes="版本库表-列表")
	@PostMapping(value = "/listIndex")
	@ResponseBody
	public Result<?> listIndex(@RequestParam Map<String,Object> mmap) {
		return twProjectFeaturesService.listIndex(mmap);
	}
	
	/**
	 *   添加
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "版本库表-添加")
	@ApiOperation(value="版本库表-添加", notes="版本库表-添加")
	@PostMapping(value = "/save")
	@ResponseBody
	public Result<?> add(@RequestParam Map<String,Object> mmap) {
		return twProjectFeaturesService.save(mmap);
	}
	
	/**
	 *  编辑
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "版本库表-编辑")
	@ApiOperation(value="版本库表-编辑", notes="版本库表-编辑")
	@PostMapping(value = "/edit")
	@ResponseBody
	public Result<?> edit(@RequestParam Map<String,Object> mmap) {
		return twProjectFeaturesService.edit(mmap);
	}
	
	/**
	 *   通过id删除
	 *
	 * @param mmap
	 * @return
	 */
	@AutoLog(value = "版本库表-通过id删除")
	@ApiOperation(value="版本库表-通过id删除", notes="版本库表-通过id删除")
	@PostMapping(value = "/delete")
	@ResponseBody
	public Result<?> delete(@RequestParam Map<String,Object> mmap) {
		return twProjectFeaturesService.delete(mmap);
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "版本库表-批量删除")
	@ApiOperation(value="版本库表-批量删除", notes="版本库表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.twProjectFeaturesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "版本库表-通过id查询")
	@ApiOperation(value="版本库表-通过id查询", notes="版本库表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TwProjectFeatures twProjectFeatures = twProjectFeaturesService.getById(id);
		if(twProjectFeatures==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(twProjectFeatures);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param twProjectFeatures
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TwProjectFeatures twProjectFeatures) {
        return super.exportXls(request, twProjectFeatures, TwProjectFeatures.class, "版本库表");
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
        return super.importExcel(request, response, TwProjectFeatures.class);
    }

}
