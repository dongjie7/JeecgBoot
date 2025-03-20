package org.jeecg.modules.erp.goods.entity;

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
 * @Description: erp_goods_property
 * @Author: nbacheng
 * @Date:   2022-08-29
 * @Version: V1.0
 */
@Data
@TableName("erp_goods_property")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="erp_goods_property对象", description="erp_goods_property")
public class ErpGoodsProperty implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**是否必填*/
	@Excel(name = "是否必填", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否必填")
    private Integer isRequired;
	/**录入类型*/
	@Excel(name = "录入类型", width = 15, dicCode = "erp_goods_column_type")
	@Dict(dicCode = "erp_goods_column_type")
    @ApiModelProperty(value = "录入类型")
    private Integer columnType;
	/**数据类型 */
	@Excel(name = "数据类型 ", width = 15, dicCode = "erp_goods_column_data_type")
	@Dict(dicCode = "erp_goods_column_data_type")
    @ApiModelProperty(value = "数据类型 ")
    private Integer columnDataType;
	/**属性类别*/
	@Excel(name = "属性类别", width = 15, dicCode = "erp_goods_property_type")
	@Dict(dicCode = "erp_goods_property_type")
    @ApiModelProperty(value = "属性类别")
    private Integer propertyType;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "erp_status")
	@Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
