package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.dto.ReportExcelDto;
import org.jeecg.modules.estar.bs.dto.ReportShareDto;
import org.jeecg.modules.estar.bs.entity.BsReportExcel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大屏Excel报表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
public interface IBsReportExcelService extends IService<BsReportExcel> {

	ReportExcelDto detailByReportCode(String reportCode);

	ReportExcelDto preview(ReportExcelDto reportExcelDto);

	String exportExcel(ReportExcelDto reportExcelDto);

	String insertShare(ReportShareDto dto);

}
