package org.jeecg.modules.estar.bs.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;

import org.jeecg.modules.estar.bs.dto.ChartDto;
import org.jeecg.modules.estar.bs.dto.ReportDashboardObjectDto;
import org.jeecg.modules.estar.bs.dto.ReportShareDto;
import org.jeecg.modules.estar.bs.entity.BsReportDashboard;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardService;
import org.jeecg.modules.estar.bs.service.IBsReportShareService;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;

 /**
 * @Description: 大屏看板表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Api(tags="大屏看板表")
@RestController
@RequestMapping("/bs/bsReportDashboard")
@Slf4j
public class BsReportDashboardController extends JeecgController<BsReportDashboard, IBsReportDashboardService> {
	@Autowired
	private IBsReportDashboardService bsReportDashboardService;
	
	@Autowired
    private IBsReportShareService reportShareService;
	
	/**
     * 预览、查询大屏详情
     * @param reportCode
     * @return
     */
    @GetMapping({"/{reportCode}"})
    public Result<?> detail(@PathVariable("reportCode") String reportCode) {
        return Result.OK(bsReportDashboardService.getDetail(reportCode));
    }

    /**
     * 保存大屏设计
     * @param dto
     * @return
     */
    @PostMapping
    public Result<?> insert(@RequestBody ReportDashboardObjectDto dto) {
    	bsReportDashboardService.insertDashboard(dto);
        return Result.OK("保存成功！");
    }


    /**
     * 获取去单个图层数据
     * @param dto
     * @return
     */
    @PostMapping("/getData")
    public Result<?> getData(@RequestBody ChartDto dto) {
        return Result.OK(bsReportDashboardService.getChartData(dto));
    }


    /**
     * 导出大屏
     * @param reportCode
     * @return
     * @throws Exception 
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDashboard(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam("reportCode") String reportCode, @RequestParam(value = "showDataSet",required = false, defaultValue = "1") Integer showDataSet) throws Exception {
        return bsReportDashboardService.exportDashboard(request, response, reportCode, showDataSet);
    }

    /**
     * 导入大屏
     * @param file  导入的zip文件
     * @param reportCode
     * @return
     */
    @PostMapping("/import/{reportCode}")
    public Result<?> importDashboard(@RequestParam("file") MultipartFile file, @PathVariable("reportCode") String reportCode) {
    	bsReportDashboardService.importDashboard(file, reportCode);
    	return Result.OK("导入成功！");
    }

    @PostMapping("/share")
    public Result<?> share(@Validated @RequestBody ReportShareDto dto) {
        return Result.OK(reportShareService.insertShare(dto));
    }

    

}
