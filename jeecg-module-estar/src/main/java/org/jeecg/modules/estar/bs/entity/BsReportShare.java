package org.jeecg.modules.estar.bs.entity;

import java.io.Serializable;
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
 * @Description: 大屏分享表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Data
@TableName("bs_report_share")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_report_share对象", description="大屏分享表")
public class BsReportShare implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**分享编码*/
	@Excel(name = "分享编码", width = 15)
    @ApiModelProperty(value = "分享编码")
    private String shareCode;
	/**分享有效期类型*/
	@Excel(name = "分享有效期类型", width = 15)
    @ApiModelProperty(value = "分享有效期类型")
    private Integer shareValidType;
	/**分享有效期*/
	@Excel(name = "分享有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "分享有效期")
    private java.util.Date shareValidTime;
	/**分享token*/
	@Excel(name = "分享token", width = 15)
    @ApiModelProperty(value = "分享token")
    private String shareToken;
	/**分享url*/
	@Excel(name = "分享url", width = 15)
    @ApiModelProperty(value = "分享url")
    private String shareUrl;
	/**分享码*/
	@Excel(name = "分享码", width = 15)
    @ApiModelProperty(value = "分享码")
    private String sharePassword;
	/**报表编码*/
	@Excel(name = "报表编码", width = 15)
    @ApiModelProperty(value = "报表编码")
    private String reportCode;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "erp_status")
	@Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private String status;
	
	@TableField(exist = false)
	private boolean sharePasswordFlag;
	/** 大屏类型 report excel */
	@TableField(exist = false)
	private String reportType;
	
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
