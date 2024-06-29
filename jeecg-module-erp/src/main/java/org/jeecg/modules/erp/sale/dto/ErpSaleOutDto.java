package org.jeecg.modules.erp.sale.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 销售出库单DTO
 * @Author: nbacheng
 * @Date:   2023-02-15
 * @Version: V1.0
 */

@Data
public class ErpSaleOutDto implements Serializable {
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
    
    /**客户ID*/
    private String customerId;

    /** 客户编号*/
    private String csCode;

    /** 客户名称*/
    private String csName;
    
    /**付款日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "付款日期")
    private java.util.Date paymentDate;
    
    /**销售员ID*/
    private String salerId;

    /** 销售员姓名*/
    private String salerName;

    /** 销售订单ID*/
    private String saleOrderId;

    /** 销售订单号*/
    private String saleOrderCode;

    /** 销售数量*/
    private Integer totalNum;
    
    /** 赠品数量*/
    private Integer totalGiftNum;

    /** 销售金额*/
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
