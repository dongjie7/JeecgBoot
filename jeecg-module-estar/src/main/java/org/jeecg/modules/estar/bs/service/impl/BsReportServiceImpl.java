package org.jeecg.modules.estar.bs.service.impl;

import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.ReportDto;
import org.jeecg.modules.estar.bs.entity.BsReport;
import org.jeecg.modules.estar.bs.entity.BsReportDashboard;
import org.jeecg.modules.estar.bs.entity.BsReportDashboardWidget;
import org.jeecg.modules.estar.bs.entity.BsReportExcel;
import org.jeecg.modules.estar.bs.enums.ReportTypeEnum;
import org.jeecg.modules.estar.bs.mapper.BsReportDashboardMapper;
import org.jeecg.modules.estar.bs.mapper.BsReportMapper;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardService;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardWidgetService;
import org.jeecg.modules.estar.bs.service.IBsReportExcelService;
import org.jeecg.modules.estar.bs.service.IBsReportService;
import org.jeecg.modules.estar.bs.util.EstarBeanUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 大屏报表
 * @Author: nbacheng
 * @Date:   2023-03-22
 * @Version: V1.0
 */
@Service
public class BsReportServiceImpl extends ServiceImpl<BsReportMapper, BsReport> implements IBsReportService {
	 @Autowired
	    private BsReportMapper reportMapper;
	    //@Autowired
	    //private IBsReportService reportService;
	    //@Autowired
	    //private IBsReportDashboardService reportDashboardService;
	    @Autowired
	    private BsReportDashboardMapper reportDashboardMapper; 
	    @Autowired
	    private IBsReportDashboardWidgetService reportDashboardWidgetService;
	    @Autowired
	    private IBsReportExcelService reportExcelService;

	    /**
	     * 下载次数+1
	     *
	     * @param reportCode
	     */
	    @Override
	    public void downloadStatistics(String reportCode) {
	    	LambdaQueryWrapper<BsReport> queryWrapperReport = Wrappers.lambdaQuery();
	        queryWrapperReport.eq(BsReport::getReportCode, reportCode);
	        //BsReport bsreport = reportService.getOne(queryWrapperReport);
	        BsReport bsreport = reportMapper.selectOne(queryWrapperReport); 
	        if (null != bsreport) {
	            Integer downloadCount = bsreport.getDownloadCount();
	            if (null == downloadCount) {
	                downloadCount = 0;
	            }else {
	                downloadCount++;
	            }
	            bsreport.setDownloadCount(downloadCount);
	            updateById(bsreport);
	        }

	    }

	    @Override
	    @Transactional(rollbackFor = Exception.class)
	    public String copy(ReportDto dto) {
	        if (null == dto.getId()) {
	        	Result.error(ResponseCode.NOT_NULL, "id");
	        	return null; 
	        }
	        if (StringUtils.isBlank(dto.getReportCode())) {
	        	Result.error(ResponseCode.NOT_NULL, "报表编码");
	        	return null; 
	        }
	        LambdaQueryWrapper<BsReport> queryWrapperReport = Wrappers.lambdaQuery();
	        queryWrapperReport.eq(BsReport::getId, dto.getId());
	        //BsReport report = reportService.getOne(queryWrapperReport);
	        BsReport report = reportMapper.selectOne(queryWrapperReport);
	        String reportCode = report.getReportCode();
	        BsReport copyReport = copyReport(report, dto);
	        //复制主表数据
	        copyReport.setId(null);
	        save(copyReport);
	        String copyReportCode = copyReport.getReportCode();
	        String reportType = report.getReportType();
	        switch (reportType) {
	            case "1":
	                //查询看板
	            	LambdaQueryWrapper<BsReportDashboard> queryWrapper = Wrappers.lambdaQuery();
	            	queryWrapper.eq(BsReportDashboard::getReportCode, reportCode);
	                //BsReportDashboard reportDashboard = reportDashboardService.getOne(queryWrapper);
	            	BsReportDashboard reportDashboard = reportDashboardMapper.selectOne(queryWrapper);
	                if (null != reportDashboard) {
	                    reportDashboard.setId(null);
	                    reportDashboard.setReportCode(copyReportCode);
	                    //reportDashboardService.save(reportDashboard);
	                    reportDashboardMapper.insert(reportDashboard);
	                }

	                //查询组件
	                LambdaQueryWrapper<BsReportDashboardWidget> queryWrapperWidget = Wrappers.lambdaQuery();
	                queryWrapperWidget.eq(BsReportDashboardWidget::getReportCode, reportCode);
	                List<BsReportDashboardWidget> reportDashboardWidgetList = reportDashboardWidgetService.list(queryWrapperWidget);
	                if (!CollectionUtils.isEmpty(reportDashboardWidgetList)) {
	                    String finalCopyReportCode = copyReportCode;
	                    reportDashboardWidgetList.forEach(reportDashboardWidget -> {
	                        reportDashboardWidget.setId(null);
	                        reportDashboardWidget.setReportCode(finalCopyReportCode);
	                    });
	                    reportDashboardWidgetService.saveBatch(reportDashboardWidgetList);
	                }

	                break;
	            case "2":
	            	LambdaQueryWrapper<BsReportExcel> queryWrapperExcel = Wrappers.lambdaQuery();
	            	queryWrapperExcel.eq(BsReportExcel::getReportCode, reportCode);
	                BsReportExcel reportExcel = reportExcelService.getOne(queryWrapperExcel);
	                if (null != reportExcel) {
	                    reportExcel.setId(null);
	                    reportExcel.setReportCode(copyReportCode);
	                    reportExcelService.save(reportExcel);
	                }

	                break;
	            default:
	        }
			return reportType;
	    }

	    private BsReport copyReport(BsReport report, ReportDto dto){
	        //复制主表数据
	        BsReport copyReport = new BsReport();
	        EstarBeanUtils.copyAndFormatter(report, copyReport);
	        copyReport.setReportCode(dto.getReportCode());
	        copyReport.setReportName(dto.getReportName());
	        return copyReport;
	    }

		@Override
		@Transactional(rollbackFor = Exception.class)
		public void removeAll(String id) {
            LambdaQueryWrapper<BsReport> queryWrapperReport = Wrappers.lambdaQuery();
	        queryWrapperReport.eq(BsReport::getId, id);
	        //BsReport delReport = reportService.getOne(queryWrapperReport);
	        BsReport delReport = reportMapper.selectOne(queryWrapperReport);
            if (null == delReport) {
                return;
            }
            String reportCode = delReport.getReportCode();
            String reportType = delReport.getReportType();
            switch (reportType) {
                case "1":
                    LambdaQueryWrapper<BsReportDashboard> reportDashboardLambdaQueryWrapper = Wrappers.lambdaQuery();
                    reportDashboardLambdaQueryWrapper.eq(BsReportDashboard::getReportCode, reportCode);
                    //reportDashboardService.remove(reportDashboardLambdaQueryWrapper);
                    

                    LambdaQueryWrapper<BsReportDashboardWidget> reportDashboardWidgetLambdaQueryWrapper = Wrappers.lambdaQuery();
                    reportDashboardWidgetLambdaQueryWrapper.eq(BsReportDashboardWidget::getReportCode, reportCode);
                    reportDashboardWidgetService.remove(reportDashboardWidgetLambdaQueryWrapper);

                    break;
                case "2":
                    LambdaQueryWrapper<BsReportExcel> reportExcelLambdaQueryWrapper = Wrappers.lambdaQuery();
                    reportExcelLambdaQueryWrapper.eq(BsReportExcel::getReportCode, reportCode);
                    reportExcelService.remove(reportExcelLambdaQueryWrapper);
                    break;
                default:
            }
            //reportService.removeById(id);
            reportMapper.deleteById(id);
			
		}

		@Override
		@Transactional(rollbackFor = Exception.class)
		public void removeAllIds(Collection<? extends Serializable> idList) {
			idList.forEach(id -> {
                LambdaQueryWrapper<BsReport> queryWrapperReport = Wrappers.lambdaQuery();
    	        queryWrapperReport.eq(BsReport::getId, id);
    	        //BsReport delReport = reportService.getOne(queryWrapperReport);
    	        BsReport delReport = reportMapper.selectOne(queryWrapperReport);
                if (null == delReport) {
                    return;
                }
                String reportCode = delReport.getReportCode();
                String reportType = delReport.getReportType();
                switch (ReportTypeEnum.valueOf(reportType)) {
                    case report_screen:
                        LambdaQueryWrapper<BsReportDashboard> reportDashboardLambdaQueryWrapper = Wrappers.lambdaQuery();
                        reportDashboardLambdaQueryWrapper.eq(BsReportDashboard::getReportCode, reportCode);
                        //reportDashboardService.remove(reportDashboardLambdaQueryWrapper);
                        reportDashboardMapper.delete(reportDashboardLambdaQueryWrapper);
                        LambdaQueryWrapper<BsReportDashboardWidget> reportDashboardWidgetLambdaQueryWrapper = Wrappers.lambdaQuery();
                        reportDashboardWidgetLambdaQueryWrapper.eq(BsReportDashboardWidget::getReportCode, reportCode);
                        reportDashboardWidgetService.remove(reportDashboardWidgetLambdaQueryWrapper);

                        break;
                    case report_excel:
                        LambdaQueryWrapper<BsReportExcel> reportExcelLambdaQueryWrapper = Wrappers.lambdaQuery();
                        reportExcelLambdaQueryWrapper.eq(BsReportExcel::getReportCode, reportCode);
                        reportExcelService.remove(reportExcelLambdaQueryWrapper);
                        break;
                    default:
                }
            });
			//reportService.removeByIds(idList);
			reportMapper.deleteBatchIds(idList);
			
		}
	}
