package org.jeecg.modules.flowable.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: WangYuZhou
 * @create: 2022-10-01 01:04
 * @description: 开关返回数据类信息
 **/
@Data
public class SwitchDataInfo implements Serializable {

    @ApiModelProperty(value = "是开关时-数据文本")
    private String activeText;

    @ApiModelProperty(value = "是开关时-数据值")
    private String activeValue;

    @ApiModelProperty(value = "否开关时-数据文本")
    private String inactiveText;

    @ApiModelProperty(value = "否开关时-数据值")
    private String inactiveValue;
}
