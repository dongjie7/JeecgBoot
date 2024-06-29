package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.dto.ChartDto;
import org.jeecg.modules.estar.bs.dto.ReportDashboardObjectDto;
import org.jeecg.modules.estar.bs.entity.BsReportDashboard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大屏看板表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
public interface IBsReportDashboardService extends IService<BsReportDashboard> {

	/***
     * 查询详情
     *
     * @param reportCode
     */
    ReportDashboardObjectDto getDetail(String reportCode);

    /***
     * 保存大屏设计
     *
     * @param dto
     */
    void insertDashboard(ReportDashboardObjectDto dto);


    /**
     * 获取单个图表数据
     * @param dto
     * @return
     */
    Object getChartData(ChartDto dto);


    /**
     * 导出大屏，zip文件
     * @param request
     * @param response
     * @param reportCode
     * @return
     * @throws Exception 
     */
    ResponseEntity<byte[]> exportDashboard(HttpServletRequest request, HttpServletResponse response, String reportCode, Integer showDataSet) throws Exception;

    /**
     * 导入大屏zip
     * @param file
     * @param reportCode
     * @return
     */
    void importDashboard(MultipartFile file, String reportCode);
}
