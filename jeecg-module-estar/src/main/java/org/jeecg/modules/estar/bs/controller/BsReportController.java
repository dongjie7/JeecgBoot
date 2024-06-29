package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.bs.dto.ReportDto;
import org.jeecg.modules.estar.bs.entity.BsReport;
import org.jeecg.modules.estar.bs.service.IBsReportService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 大屏报表
 * @Author: nbacheng
 * @Date:   2023-03-22
 * @Version: V1.0
 */
@Api(tags="大屏报表")
@RestController
@RequestMapping("/bs/bsReport")
@Slf4j
public class BsReportController extends JeecgController<BsReport, IBsReportService> {
	@Autowired
	private IBsReportService bsReportService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "大屏报表-分页列表查询")
	@ApiOperation(value="大屏报表-分页列表查询", notes="大屏报表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsReport bsReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsReport> queryWrapper = QueryGenerator.initQueryWrapper(bsReport, req.getParameterMap());
		Page<BsReport> page = new Page<BsReport>(pageNo, pageSize);
		IPage<BsReport> pageList = bsReportService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsReport
	 * @return
	 */
	@AutoLog(value = "大屏报表-添加")
	@ApiOperation(value="大屏报表-添加", notes="大屏报表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsReport bsReport) {
		bsReportService.save(bsReport);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsReport
	 * @return
	 */
	@AutoLog(value = "大屏报表-编辑")
	@ApiOperation(value="大屏报表-编辑", notes="大屏报表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsReport bsReport) {
		bsReportService.updateById(bsReport);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *  编辑
	 *
	 * @param ReportDto
	 * @return
	 */
	@PostMapping("/copy")
	@AutoLog(value = "大屏报表-复制")
	@ApiOperation(value="大屏报表-复制", notes="大屏报表-复制")
    public Result<?> copy(@RequestBody ReportDto dto) {
		if(bsReportService.copy(dto) != null) {
			return Result.OK("复制成功!");
		}
		else {
			return Result.error("复制失败!");
		}
    }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏报表-通过id删除")
	@ApiOperation(value="大屏报表-通过id删除", notes="大屏报表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsReportService.removeAll(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "大屏报表-批量删除")
	@ApiOperation(value="大屏报表-批量删除", notes="大屏报表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsReportService.removeAllIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏报表-通过id查询")
	@ApiOperation(value="大屏报表-通过id查询", notes="大屏报表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsReport bsReport = bsReportService.getById(id);
		if(bsReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsReport);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsReport
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsReport bsReport) {
        return super.exportXls(request, bsReport, BsReport.class, "大屏报表");
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
        return super.importExcel(request, response, BsReport.class);
    }

}
