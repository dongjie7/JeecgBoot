package org.jeecg.modules.erp.goods.dto;

import java.io.Serializable;

import org.jeecg.common.aspect.annotation.Dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ErpGoodsDto implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
    private String id;
    
	/**编号*/
    private String code;
    
	/**名称*/
    private String name;
    
	/**SPU编号*/
    //private java.lang.String spuId;
    
	/**类别编号*/
    private String categoryId;
    
    /**类别名称*/
    private String categoryName;
    
	/**品牌编号*/
    private String brandId;
    
    /**品牌名称*/
    private String brandName;
    
	/**规格*/
    private String spec;
    
	/**单位*/
    private String unit;
    
    /**采购价格*/
    private java.math.BigDecimal purchasePrice;
    
    /**销售价格*/
    private java.math.BigDecimal salePrice;
    
    /**零售价格*/
    private java.math.BigDecimal retailPrice;
    
    /**税率（%）*/
    private java.math.BigDecimal taxRate;
    
    /**销项税率（%）*/
    private java.math.BigDecimal saleTaxRate;
    
    /**数量*/
    private Integer num;
    
    /**总价格*/
    private java.math.BigDecimal totalPrice;
    
    /**库存数量*/
    private Integer stockNum;
    
    @Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private Integer status;

}