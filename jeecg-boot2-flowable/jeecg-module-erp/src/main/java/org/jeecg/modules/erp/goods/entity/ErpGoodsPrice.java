package org.jeecg.modules.erp.goods.entity;

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
 * @Description: 商品价格信息
 * @Author: nbacheng
 * @Date:   2023-03-08
 * @Version: V1.0
 */
@ApiModel(value="erp_goods_price对象", description="商品价格信息")
@Data
@TableName("erp_goods_price")
public class ErpGoodsPrice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**采购价*/
	@Excel(name = "采购价", width = 15)
    @ApiModelProperty(value = "采购价")
    private java.math.BigDecimal purchase;
	/**销售价*/
	@Excel(name = "销售价", width = 15)
    @ApiModelProperty(value = "销售价")
    private java.math.BigDecimal sale;
	/**零售价*/
	@Excel(name = "零售价", width = 15)
    @ApiModelProperty(value = "零售价")
    private java.math.BigDecimal retail;
}
