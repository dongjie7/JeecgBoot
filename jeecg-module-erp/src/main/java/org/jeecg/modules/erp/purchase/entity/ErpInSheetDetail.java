package org.jeecg.modules.erp.purchase.entity;

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
 * @Description: erp_in_sheet_detail
 * @Author: nbacheng
 * @Date:   2022-09-01
 * @Version: V1.0
 */
@ApiModel(value="erp_in_sheet_detail对象", description="erp_in_sheet_detail")
@Data
@TableName("erp_in_sheet_detail")
public class ErpInSheetDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**入库单ID*/
    @ApiModelProperty(value = "入库单ID")
    private String sheetId;
	/**商品ID*/
	@Excel(name = "商品ID", width = 15)
    @ApiModelProperty(value = "商品ID")
    private String goodsId;
	/**采购数量*/
	@Excel(name = "采购数量", width = 15)
    @ApiModelProperty(value = "采购数量")
    private Integer orderNum;
	/**采购价*/
	@Excel(name = "采购价", width = 15)
    @ApiModelProperty(value = "采购价")
    private java.math.BigDecimal taxPrice;
	/**是否赠品*/
	@Excel(name = "是否赠品", width = 15)
    @ApiModelProperty(value = "是否赠品")
    private Integer isGift;
	/**税率（%）*/
	@Excel(name = "税率（%）", width = 15)
    @ApiModelProperty(value = "税率（%）")
    private java.math.BigDecimal taxRate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String description;
	/**排序编号*/
	@Excel(name = "排序编号", width = 15)
    @ApiModelProperty(value = "排序编号")
    private Integer orderNo;
	/**采购订单明细ID*/
	@Excel(name = "采购订单明细ID", width = 15)
    @ApiModelProperty(value = "采购订单明细ID")
    private String purchaseOrderDetailId;
	/**已退货数量*/
	@Excel(name = "已退货数量", width = 15)
    @ApiModelProperty(value = "已退货数量")
    private Integer returnNum;
}
