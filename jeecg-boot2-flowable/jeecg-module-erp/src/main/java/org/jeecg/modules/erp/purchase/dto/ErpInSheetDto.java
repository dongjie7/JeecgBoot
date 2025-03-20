package org.jeecg.modules.erp.purchase.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 采购入库单DTO
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */

@Data
public class ErpInSheetDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**ID*/
    private String id;

    /**单号*/
    private String code;

    /**仓库ID*/
    private String scId;
    
    /**仓库编号*/
    private String wsCode;

    /** 仓库名称*/
    private String wsName;
    
    /**供应商ID*/
    private String supplierId;

    /** 供应商编号*/
    private String slCode;

    /** 供应商名称*/
    private String slName;
    
    /**付款日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "付款日期")
    private java.util.Date paymentDate;
    
    /**入库日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期")
    private java.util.Date inDate;
    
    /**采购员ID*/
    private String purchaserId;

    /** 采购员姓名*/
    private String purchaserName;

    /**采购单ID*/
    private String purchaseOrderId;

    /** 商品数量*/
    private Integer totalNum;
    
    /** 赠品数量*/
    private Integer totalGiftNum;

    /** 入库金额*/
    private BigDecimal totalAmount;

    /** 备注*/
    private String description;

    /** 创建人*/
    private String createBy;

    /** 创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    /** 审核人*/
    private String approveBy;

    /** 审核时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核时间")
    private java.util.Date approveTime;

    /** 状态*/
    private Integer status;

    /** 拒绝原因*/
    private String refuseReason;

    /** 结算状态*/
    private Integer settleStatus;
}
