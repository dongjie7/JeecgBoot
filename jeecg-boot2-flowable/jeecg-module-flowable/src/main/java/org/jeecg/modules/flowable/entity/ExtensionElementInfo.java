package org.jeecg.modules.flowable.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("条件表达式信息")
public class ExtensionElementInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	/**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 表达式
     */
    @ApiModelProperty("表达式")
    private String expression;
}
