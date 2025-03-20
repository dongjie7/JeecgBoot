
package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
*
* @description 大屏设计 dto
* @author nbacheng
* @date 2023-03-28 
**/
@Data
public class ReportDashboardObjectDto implements Serializable {

    /** 报表编码 */
    private String reportCode;
    /**
     * 报表编码
     */
    private ReportDashboardDto dashboard;

    /**
     * 大屏画布中的组件
     */
    private List<ReportDashboardWidgetDto> widgets;

}
