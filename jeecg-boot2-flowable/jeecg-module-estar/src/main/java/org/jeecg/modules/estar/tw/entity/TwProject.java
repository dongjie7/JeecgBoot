package org.jeecg.modules.estar.tw.entity;

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
 * @Description: 项目表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Data
@TableName("tw_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_project对象", description="项目表")
public class TwProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**封面*/
	@Excel(name = "封面", width = 15)
    @ApiModelProperty(value = "封面")
    private String cover;
	/**控制类型*/
	@Excel(name = "控制类型", width = 15)
    @ApiModelProperty(value = "控制类型")
    private String accessControlType;
	/**白名单*/
	@Excel(name = "白名单", width = 15)
    @ApiModelProperty(value = "白名单")
    private String whiteList;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private Integer deleted;
	/**项目模板*/
	@Excel(name = "项目模板", width = 15, dictTable = "tw_project_template", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tw_project_template", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "项目模板")
    private String templateId;
	/**进度*/
	@Excel(name = "进度", width = 15)
    @ApiModelProperty(value = "进度")
    private Double schedule;
	/**组织编码*/
	@Excel(name = "组织编码", width = 15, dictTable = "tw_organization", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tw_organization", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "组织编码")
    private String organizationId;
	/**删除时间*/
	@Excel(name = "删除时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "删除时间")
    private java.util.Date deletedTime;
	/**是否私有*/
	@Excel(name = "是否私有", width = 15)
    @ApiModelProperty(value = "是否私有")
    private Integer privated;
	/**项目前缀*/
	@Excel(name = "项目前缀", width = 15)
    @ApiModelProperty(value = "项目前缀")
    private String prefix;
	/**是否开启项目前缀*/
	@Excel(name = "是否开启项目前缀", width = 15)
    @ApiModelProperty(value = "是否开启项目前缀")
    private Integer openPrefix;
	/**是否归档*/
	@Excel(name = "是否归档", width = 15)
    @ApiModelProperty(value = "是否归档")
    private Integer archive;
	/**归档时间*/
	@Excel(name = "归档时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "归档时间")
    private java.util.Date archiveTime;
	/**是否开启任务开始时间*/
	@Excel(name = "是否开启任务开始时间", width = 15)
    @ApiModelProperty(value = "是否开启任务开始时间")
    private Integer openBeginTime;
	/**是否开启新任务默认开启隐私模式*/
	@Excel(name = "是否开启新任务默认开启隐私模式", width = 15)
    @ApiModelProperty(value = "是否开启新任务默认开启隐私模式")
    private Integer openTaskPrivate;
	/**看板风格*/
	@Excel(name = "看板风格", width = 15)
    @ApiModelProperty(value = "看板风格")
    private String taskBoardTheme;
	/**项目开始日期*/
	@Excel(name = "项目开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "项目开始日期")
    private java.util.Date beginTime;
	/**项目截止日期*/
	@Excel(name = "项目截止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "项目截止日期")
    private java.util.Date endTime;
	/**自动更新项目进度*/
	@Excel(name = "自动更新项目进度", width = 15)
    @ApiModelProperty(value = "自动更新项目进度")
    private Integer autoUpdateSchedule;
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
    
    /**详情*/
	@Excel(name = "详情", width = 15)
    @ApiModelProperty(value = "详情")
    private String description;
    
    @TableField(exist = false)
    private Integer collected;
    @TableField(exist = false)
    private String ownerName;
    @TableField(exist = false)
    private String ownerAvatar;
}
