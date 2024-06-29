package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.dto.ReportDto;
import org.jeecg.modules.estar.bs.entity.BsReport;

import java.io.Serializable;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大屏报表
 * @Author: nbacheng
 * @Date:   2023-03-22
 * @Version: V1.0
 */
public interface IBsReportService extends IService<BsReport> {
	
	/**
     * 下载次数+1
     * @param reportCode
     */
    void downloadStatistics(String reportCode);

    /**
     * 复制大屏
     * @param dto
     */
    String copy(ReportDto dto);
    
    /**
     * 删除大屏
     * @param id
     */
    void removeAll(String id);
    
    /**
     * 批量删除大屏
     * @param id
     */
    void removeAllIds(Collection<? extends Serializable> idList);
}
