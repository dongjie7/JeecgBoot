package org.jeecg.modules.erp.stock.vo;

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
 * @Description: 商品库存表
 * @Author: nbacheng
 * @Date:   2022-11-25
 * @Version: V1.0
 */
@Data
public class ErpGoodsStockVo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**仓库ID*/
	@Excel(name = "仓库ID", width = 15)
    @ApiModelProperty(value = "仓库ID")
    private String scId;
	/**商品ID*/
	@Excel(name = "商品ID", width = 15)
    @ApiModelProperty(value = "商品ID")
    private String goodsId;
	/**库存数量*/
	@Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    private Integer stockNum;
	/**含税价格*/
	@Excel(name = "含税价格", width = 15)
    @ApiModelProperty(value = "含税价格")
    private BigDecimal taxPrice;
	/**含税金额*/
	@Excel(name = "含税金额", width = 15)
    @ApiModelProperty(value = "含税金额")
    private BigDecimal taxAmount;
	/**无税价格*/
	@Excel(name = "无税价格", width = 15)
    @ApiModelProperty(value = "无税价格")
    private BigDecimal unTaxPrice;
	/**无税金额*/
	@Excel(name = "无税金额", width = 15)
    @ApiModelProperty(value = "无税金额")
    private BigDecimal unTaxAmount;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**业务单据ID*/
	@Excel(name = "业务单据ID", width = 15)
    @ApiModelProperty(value = "业务单据ID")
    private String bizId;
	/**业务单据号*/
	@Excel(name = "业务单据号", width = 15)
    @ApiModelProperty(value = "业务单据号")
    private String bizCode;
	/**业务单据明细ID*/
	@Excel(name = "业务单据明细ID", width = 15)
    @ApiModelProperty(value = "业务单据明细ID")
    private String bizDetailId;
	/**业务类型*/
	@Excel(name = "业务类型", width = 15)
    @ApiModelProperty(value = "业务类型")
    private Integer bizType;
}
