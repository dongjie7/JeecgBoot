package org.jeecg.modules.estar.bs.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: bs_data_set_param
 * @Author: nbacheng
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Data
@TableName("bs_data_set_param")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_data_set_param对象", description="bs_data_set_param")
public class BsDataSetParam implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**数据集编码*/
	@Excel(name = "数据集编码", width = 15)
    @ApiModelProperty(value = "数据集编码")
    private String setCode;
	/**参数名*/
	@Excel(name = "参数名", width = 15)
    @ApiModelProperty(value = "参数名")
    private String paramName;
	/**参数描述*/
	@Excel(name = "参数描述", width = 15)
    @ApiModelProperty(value = "参数描述")
    private String paramDesc;
	/**参数类型，字典=*/
	@Excel(name = "参数类型，字典=", width = 15)
    @ApiModelProperty(value = "参数类型，字典=")
    private String paramType;
	/**参数示例项*/
	@Excel(name = "参数示例项", width = 15)
    @ApiModelProperty(value = "参数示例项")
    private String sampleItem;
	/**0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG*/
	@Excel(name = "0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG", width = 15)
    @ApiModelProperty(value = "0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG")
    private Integer requiredFlag;
	/**js校验字段值规则，满足校验返回 true*/
	@Excel(name = "js校验字段值规则，满足校验返回 true", width = 15)
    @ApiModelProperty(value = "js校验字段值规则，满足校验返回 true")
    private String validationRules;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
	/**0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG*/
	@Excel(name = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG", width = 15)
    @ApiModelProperty(value = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
    private Integer enableFlag;
	/**0--未删除 1--已删除 DIC_NAME=DELETE_FLAG*/
	@Excel(name = "0--未删除 1--已删除 DIC_NAME=DELETE_FLAG", width = 15)
    @ApiModelProperty(value = "0--未删除 1--已删除 DIC_NAME=DELETE_FLAG")
    private Integer deleteFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**version*/
	@Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private Integer version;
}
