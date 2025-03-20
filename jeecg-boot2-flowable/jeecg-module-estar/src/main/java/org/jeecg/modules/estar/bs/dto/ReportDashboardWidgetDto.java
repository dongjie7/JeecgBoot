
package org.jeecg.modules.estar.bs.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
*
* @description 大屏看板数据渲染 dto
* @author nbacheng
* @date 2023-03-28 
**/
@Data
public class ReportDashboardWidgetDto implements Serializable {

    /**
     * 组件类型参考字典BS_PANEL_TYPE
     */
    private String type;

    /**
     * value
     */
    private ReportDashboardWidgetValueDto value;

    /**
     * options
     */
    private JSONObject options;

}
