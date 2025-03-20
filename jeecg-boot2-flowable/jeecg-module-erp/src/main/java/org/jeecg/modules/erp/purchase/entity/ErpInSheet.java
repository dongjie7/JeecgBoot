package org.jeecg.modules.erp.purchase.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: erp_in_sheet
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
@ApiModel(value="erp_in_sheet对象", description="erp_in_sheet")
@Data
@TableName("erp_in_sheet")
public class ErpInSheet implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
    private String code;
	/**仓库ID*/
	@Excel(name = "仓库ID", width = 15)
    @ApiModelProperty(value = "仓库ID")
    private String scId;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
	/**采购员ID*/
	@Excel(name = "采购员ID", width = 15)
    @ApiModelProperty(value = "采购员ID")
    private String purchaserId;
	/**付款日期*/
	@Excel(name = "付款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "付款日期")
    private Date paymentDate;
	/**入库日期*/
	@Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期")
    private Date inDate;
	/**采购单ID*/
	@Excel(name = "采购单ID", width = 15)
    @ApiModelProperty(value = "采购单ID")
    private String purchaseOrderId;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
    private Integer totalNum;
	/**赠品数量*/
	@Excel(name = "赠品数量", width = 15)
    @ApiModelProperty(value = "赠品数量")
    private Integer totalGiftNum;
	/**入库金额*/
	@Excel(name = "入库金额", width = 15)
    @ApiModelProperty(value = "入库金额")
    private java.math.BigDecimal totalAmount;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String description;
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
	/**审核人*/
	@Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private String approveBy;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核时间")
    private Date approveTime;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "erp_status")
    @Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private Integer status;
	/**拒绝原因*/
	@Excel(name = "拒绝原因", width = 15)
    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;
	/**结算状态*/
	@Excel(name = "结算状态", width = 15, dicCode = "erp_settle_status")
    @Dict(dicCode = "erp_settle_status")
    @ApiModelProperty(value = "结算状态")
    private Integer settleStatus;
}
