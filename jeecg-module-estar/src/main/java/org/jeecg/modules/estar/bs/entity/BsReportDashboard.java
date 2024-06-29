package org.jeecg.modules.estar.bs.entity;

import java.io.Serializable;
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
 * @Description: 大屏看板表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Data
@TableName("bs_report_dashboard")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_report_dashboard对象", description="大屏看板表")
public class BsReportDashboard implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**报表编码*/
	@Excel(name = "报表编码", width = 15)
    @ApiModelProperty(value = "报表编码")
    private String reportCode;
	/**看板标题*/
	@Excel(name = "看板标题", width = 15)
    @ApiModelProperty(value = "看板标题")
    private String title;
	/**宽度px*/
	@Excel(name = "宽度px", width = 15)
    @ApiModelProperty(value = "宽度px")
    private Integer width;
	/**高度px*/
	@Excel(name = "高度px", width = 15)
    @ApiModelProperty(value = "高度px")
    private Integer height;
	/**背景色*/
	@Excel(name = "背景色", width = 15)
    @ApiModelProperty(value = "背景色")
    private String backgroundColor;
	/**背景图片*/
	@Excel(name = "背景图片", width = 15)
    @ApiModelProperty(value = "背景图片")
    private String backgroundImage;
	/**工作台中的辅助线*/
	@Excel(name = "工作台中的辅助线", width = 15)
    @ApiModelProperty(value = "工作台中的辅助线")
    private String presetLine;
	/**自动刷新间隔秒*/
	@Excel(name = "自动刷新间隔秒", width = 15)
    @ApiModelProperty(value = "自动刷新间隔秒")
    private Integer refreshSeconds;
	/**排序，降序*/
	@Excel(name = "排序，降序", width = 15)
    @ApiModelProperty(value = "排序，降序")
	private Integer sort;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "erp_status")
	@Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private String status;
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
