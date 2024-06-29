
package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import org.jeecg.common.system.base.entity.JeecgEntity;


/**
*
* @description 大屏设计 dto
* @author Raod
* @date 2021-04-12 14:52:21.761
**/
@Data
public class ReportDashboardDto extends JeecgEntity implements Serializable {
    /** 报表编码 */
    private String reportCode;

    /** 看板标题 */
    private String title;

    /** 宽度px */
    private Integer width;

    /** 高度px */
    private Integer height;

    /** 背景色 */
    private String backgroundColor;

    /** 背景图片 */
    private String backgroundImage;

    /** 工作台中的辅助线 */
    private String presetLine;

    /** 自动刷新间隔秒，数据字典REFRESH_TYPE */
    private Integer refreshSeconds;

    /** 0--禁用 1--启用 */
    private String status;

    /** 排序，降序 */
    private Integer sort;

    private List<ReportDashboardWidgetDto> widgets;

}
