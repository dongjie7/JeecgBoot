package org.jeecg.modules.erp.sale.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 销售出库单明细
 * @Author: nbacheng
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@ApiModel(value="erp_sale_out_detail对象", description="销售出库单明细")
@Data
@TableName("erp_sale_out_detail")
public class ErpSaleOutDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**出库单ID*/
    @ApiModelProperty(value = "出库单ID")
    private String sheetId;
	/**商品ID*/
	@Excel(name = "商品ID", width = 15)
    @ApiModelProperty(value = "商品ID")
    private String goodsId;
	/**出库数量*/
	@Excel(name = "出库数量", width = 15)
    @ApiModelProperty(value = "出库数量")
    private Integer orderNum;
	/**参考价*/
	@Excel(name = "参考价", width = 15)
    @ApiModelProperty(value = "参考价")
    private java.math.BigDecimal oriPrice;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
    private java.math.BigDecimal taxPrice;
	/**折扣率（%）*/
	@Excel(name = "折扣率（%）", width = 15)
    @ApiModelProperty(value = "折扣率（%）")
    private java.math.BigDecimal discountRate;
	/**是否赠品*/
	@Excel(name = "是否赠品", width = 15)
    @ApiModelProperty(value = "是否赠品")
    private Integer isGift;
	/**税率（%）*/
	@Excel(name = "税率（%）", width = 15)
    @ApiModelProperty(value = "税率（%）")
    private java.math.BigDecimal taxRate;
	/**总价*/
	@Excel(name = "总价", width = 15)
    @ApiModelProperty(value = "总价")
    private java.math.BigDecimal totalPrice;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String description;
	/**排序编号*/
	@Excel(name = "排序编号", width = 15)
    @ApiModelProperty(value = "排序编号")
    private Integer orderNo;
	/**结算状态*/
	@Excel(name = "结算状态", width = 15)
    @ApiModelProperty(value = "结算状态")
    private Integer settleStatus;
	/**销售订单明细ID*/
	@Excel(name = "销售订单明细ID", width = 15)
    @ApiModelProperty(value = "销售订单明细ID")
    private String saleOrderDetailId;
	/**已退货数量*/
	@Excel(name = "已退货数量", width = 15)
    @ApiModelProperty(value = "已退货数量")
    private Integer returnNum;
}
