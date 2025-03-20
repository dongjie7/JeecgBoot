package org.jeecg.modules.estar.oa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CalendarListVo {
	/**id*/
    @ApiModelProperty(value = "id")
    private String id;
	/**名称*/
    @ApiModelProperty(value = "名称")
    private String name;
	/**颜色*/
    @ApiModelProperty(value = "颜色")
    private String color;
    /**所属人*/
    @ApiModelProperty(value = "所属人")
    private String owner;
	/**参与人*/
    @ApiModelProperty(value = "参与人")
    private String taker;
}
