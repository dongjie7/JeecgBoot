package org.jeecg.modules.estar.tw.entity;

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
 * @Description: 项目版本表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Data
@TableName("tw_project_version")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_project_version对象", description="项目版本表")
public class TwProjectVersion implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private String id;
	/**版本库名称*/
	@Excel(name = "版本库名称", width = 15)
    @ApiModelProperty(value = "版本库名称")
    private String name;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private Date updateTime;
	/**组织id*/
	@Excel(name = "组织id", width = 15)
    @ApiModelProperty(value = "组织id")
    private String organizationId;
	/**发布时间*/
	@Excel(name = "发布时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
	/**状态。0：未开始，1：进行中，2：延期发布，3：已发布*/
	@Excel(name = "状态。0：未开始，1：进行中，2：延期发布，3：已发布", width = 15)
    @ApiModelProperty(value = "状态。0：未开始，1：进行中，2：延期发布，3：已发布")
    private Integer status;
	/**进度百分比*/
	@Excel(name = "进度百分比", width = 15)
    @ApiModelProperty(value = "进度百分比")
    private Integer schedule;
	/**预计发布时间*/
	@Excel(name = "预计发布时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计发布时间")
    private Date planPublishTime;
	/**版本库编号*/
	@Excel(name = "版本库编号", width = 15)
    @ApiModelProperty(value = "版本库编号")
    private String featuresId;
}
