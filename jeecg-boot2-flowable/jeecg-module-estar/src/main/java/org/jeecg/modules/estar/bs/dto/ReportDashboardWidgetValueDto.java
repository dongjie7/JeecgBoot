
package org.jeecg.modules.estar.bs.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
*
* @description 大屏看板数据渲染 dto，已弃用
* @author nbacheng
* @date 2023-03-28
**/
@Data
public class ReportDashboardWidgetValueDto implements Serializable {
    /** 报表编码 */
    private String reportCode;

    /** 组件的渲染属性json */
    private JSONObject setup;

    /** 组件的数据属性json */
    private JSONObject data;

    /** 组件的配置属性json */
    private JSONObject collapse;

    /** 组件的大小位置属性json */
    private JSONObject position;

    private String options;

    /** 自动刷新间隔秒 */
    private Integer refreshSeconds;

    /** 0--禁用 1--启用 */
    private String status;


    /** 排序，图层的概念 */
    private Integer sort;

}
