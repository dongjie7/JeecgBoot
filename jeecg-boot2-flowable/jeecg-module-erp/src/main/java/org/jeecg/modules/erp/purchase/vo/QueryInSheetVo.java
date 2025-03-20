package org.jeecg.modules.erp.purchase.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryInSheetVo  {

  private static final long serialVersionUID = 1L;

  /** 仓库ID*/
  @ApiModelProperty(value = "仓库ID")
  private String scId;

  /** 供应商ID*/
  @ApiModelProperty("供应商ID")
  private String supplierId;

}