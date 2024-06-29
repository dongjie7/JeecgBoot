package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 项目资源关联表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Data
@TableName("tw_source_link")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_source_link对象", description="项目资源关联表")
public class TwSourceLink implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private String id;
	/**资源类型*/
	@Excel(name = "资源类型", width = 15)
    @ApiModelProperty(value = "资源类型")
    private String sourceType;
	/**资源编号*/
	@Excel(name = "资源编号", width = 15)
    @ApiModelProperty(value = "资源编号")
    private String sourceId;
	/**关联类型*/
	@Excel(name = "关联类型", width = 15)
    @ApiModelProperty(value = "关联类型")
    private String linkType;
	/**关联编号*/
	@Excel(name = "关联编号", width = 15)
    @ApiModelProperty(value = "关联编号")
    private String linkId;
	/**组织编码*/
	@Excel(name = "组织编码", width = 15)
    @ApiModelProperty(value = "组织编码")
    private String organizationId;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	
	@TableField(exist = false)
    private String title;
    @TableField(exist = false)
    private Map sourceDetail;
}
