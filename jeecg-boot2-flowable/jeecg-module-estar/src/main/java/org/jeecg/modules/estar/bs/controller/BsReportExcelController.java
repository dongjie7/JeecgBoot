package org.jeecg.modules.estar.bs.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.estar.bs.dto.ReportExcelDto;
import org.jeecg.modules.estar.bs.dto.ReportShareDto;
import org.jeecg.modules.estar.bs.entity.BsReportExcel;
import org.jeecg.modules.estar.bs.service.IBsReportExcelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 大屏Excel报表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Api(tags="大屏Excel报表")
@RestController
@RequestMapping("/bs/bsReportExcel")
@Slf4j
public class BsReportExcelController extends JeecgController<BsReportExcel, IBsReportExcelService> {
	@Autowired
	private IBsReportExcelService bsReportExcelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsReportExcel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "大屏Excel报表-分页列表查询")
	@ApiOperation(value="大屏Excel报表-分页列表查询", notes="大屏Excel报表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsReportExcel bsReportExcel,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BsReportExcel> queryWrapper = QueryGenerator.initQueryWrapper(bsReportExcel, req.getParameterMap());
		Page<BsReportExcel> page = new Page<BsReportExcel>(pageNo, pageSize);
		IPage<BsReportExcel> pageList = bsReportExcelService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	@AutoLog(value = "大屏Excel报表-详情")
	@ApiOperation(value="大屏Excel报表-详情", notes="大屏Excel报表-详情")
	@GetMapping("/detailByReportCode/{reportCode}")
    public Result<?> detailByReportCode(@PathVariable String reportCode) {
        ReportExcelDto reportExcelDto = bsReportExcelService.detailByReportCode(reportCode);
        return Result.OK(reportExcelDto);
    }

	@AutoLog(value = "大屏Excel报表-预览")
	@ApiOperation(value="大屏Excel报表-预览", notes="大屏Excel报表-预览")
    @PostMapping("/preview")
    public Result<?> preview(@RequestBody ReportExcelDto reportExcelDto) {
        ReportExcelDto result = bsReportExcelService.preview(reportExcelDto);
        return Result.OK(result);
    }


	@AutoLog(value = "大屏Excel报表-报表导出")
	@ApiOperation(value="大屏Excel报表-报表导出", notes="大屏Excel报表-报表导出")
    @PostMapping("/exportExcel")
    public Result<?> exportExcel(@RequestBody ReportExcelDto reportExcelDto) {

        return Result.OK(bsReportExcelService.exportExcel(reportExcelDto));
    }

//    @PostMapping("/exportPdf")
//    public ResponseBean exportPdf(@RequestBody ReportExcelDto reportExcelDto) {
//        reportExcelService.exportPdf(reportExcelDto);
//        return Result.OK("导出pdf成功");
//    }

	@AutoLog(value = "大屏Excel报表-excel分享")
	@ApiOperation(value="大屏Excel报表-excel分享", notes="大屏Excel报表-excel分享")
    @PostMapping("/share")
    public Result<?> share(@Validated @RequestBody ReportShareDto dto) {
        return Result.OK(bsReportExcelService.insertShare(dto));
    }
	
	/**
	 *   添加
	 *
	 * @param bsReportExcel
	 * @return
	 */
	@AutoLog(value = "大屏Excel报表-添加")
	@ApiOperation(value="大屏Excel报表-添加", notes="大屏Excel报表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsReportExcel bsReportExcel) {
		bsReportExcelService.save(bsReportExcel);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsReportExcel
	 * @return
	 */
	@AutoLog(value = "大屏Excel报表-编辑")
	@ApiOperation(value="大屏Excel报表-编辑", notes="大屏Excel报表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsReportExcel bsReportExcel) {
		bsReportExcelService.updateById(bsReportExcel);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏Excel报表-通过id删除")
	@ApiOperation(value="大屏Excel报表-通过id删除", notes="大屏Excel报表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsReportExcelService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "大屏Excel报表-批量删除")
	@ApiOperation(value="大屏Excel报表-批量删除", notes="大屏Excel报表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsReportExcelService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "大屏Excel报表-通过id查询")
	@ApiOperation(value="大屏Excel报表-通过id查询", notes="大屏Excel报表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsReportExcel bsReportExcel = bsReportExcelService.getById(id);
		if(bsReportExcel==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(bsReportExcel);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsReportExcel
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsReportExcel bsReportExcel) {
        return super.exportXls(request, bsReportExcel, BsReportExcel.class, "大屏Excel报表");
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
        return super.importExcel(request, response, BsReportExcel.class);
    }

}
