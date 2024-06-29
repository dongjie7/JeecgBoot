package org.jeecg.modules.estar.oa.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScheduleListVO{
    @ApiModelProperty(value = "日程主键")
    private String id;
    @ApiModelProperty(value = "日程标题")
    private String title;
    @ApiModelProperty(value = "开始时间")
    private long startTime;
    @ApiModelProperty(value = "开始时间")
    private long endTime;
    @ApiModelProperty(value = "颜色")
    private String color;
    @ApiModelProperty(value = "全天")
    private String allday;
}
