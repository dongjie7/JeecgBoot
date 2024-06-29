package org.jeecg.modules.erp.sale.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySaleOutVo  {

  private static final long serialVersionUID = 1L;

  /** 仓库ID*/
  @ApiModelProperty(value = "仓库ID")
  private String scId;

  /** 客户ID*/
  @ApiModelProperty("客户ID")
  private String customerId;

}