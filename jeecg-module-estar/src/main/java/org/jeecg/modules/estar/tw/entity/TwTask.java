package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 项目任务表
 * @Author: nbacheng
 * @Date:   2023-07-01
 * @Version: V1.0
 */
@Data
@TableName("tw_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_task对象", description="项目任务表")
public class TwTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private String id;
	/**项目编号*/
	@Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private String projectId;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
	/**紧急程度*/
	@Excel(name = "紧急程度", width = 15)
    @ApiModelProperty(value = "紧急程度")
    private Integer pri;
	/**执行状态*/
	@Excel(name = "执行状态", width = 15)
    @ApiModelProperty(value = "执行状态")
    private String executeStatus;
	/**详情*/
	@Excel(name = "详情", width = 15)
    @ApiModelProperty(value = "详情")
    private String description;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**指派给谁*/
	@Excel(name = "指派给谁", width = 15)
    @ApiModelProperty(value = "指派给谁")
    private String assignTo;
	/**回收站*/
	@Excel(name = "回收站", width = 15)
    @ApiModelProperty(value = "回收站")
    private Integer deleted;
	/**任务列表*/
	@Excel(name = "任务列表", width = 15)
    @ApiModelProperty(value = "任务列表")
    private String stageId;
	/**任务标签*/
	@Excel(name = "任务标签", width = 15)
    @ApiModelProperty(value = "任务标签")
    private String taskTag;
	/**是否完成*/
	@Excel(name = "是否完成", width = 15)
    @ApiModelProperty(value = "是否完成")
    private Integer done;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date beginTime;
	/**截止时间*/
	@Excel(name = "截止时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "截止时间")
    private java.util.Date endTime;
	/**提醒时间*/
	@Excel(name = "提醒时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "提醒时间")
    private java.util.Date remindTime;
	/**父任务id*/
	@Excel(name = "父任务id", width = 15)
    @ApiModelProperty(value = "父任务id")
    private String pid;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**点赞数*/
	@Excel(name = "点赞数", width = 15)
    @ApiModelProperty(value = "点赞数")
    private Integer likes;
	/**收藏数*/
	@Excel(name = "收藏数", width = 15)
    @ApiModelProperty(value = "收藏数")
    private Integer star;
	/**删除时间*/
	@Excel(name = "删除时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "删除时间")
    private java.util.Date deletedTime;
	/**是否隐私模式*/
	@Excel(name = "是否隐私模式", width = 15)
    @ApiModelProperty(value = "是否隐私模式")
    private Integer privated;
	/**任务id编号*/
	@Excel(name = "任务id编号", width = 15)
    @ApiModelProperty(value = "任务id编号")
    private Integer idNum;
	/**上级任务路径*/
	@Excel(name = "上级任务路径", width = 15)
    @ApiModelProperty(value = "上级任务路径")
    private String path;
	/**进度百分比*/
	@Excel(name = "进度百分比", width = 15)
    @ApiModelProperty(value = "进度百分比")
    private Integer schedule;
	/**版本id*/
	@Excel(name = "版本id", width = 15)
    @ApiModelProperty(value = "版本id")
    private String versionId;
	/**版本库id*/
	@Excel(name = "版本库id", width = 15)
    @ApiModelProperty(value = "版本库id")
    private String featuresId;
	/**预估工时*/
	@Excel(name = "预估工时", width = 15)
    @ApiModelProperty(value = "预估工时")
    private Integer workTime;
	/**执行状态.0:未开始,1:已完成,2:进行中,3:挂起,4:测试中*/
	@Excel(name = "执行状态.0:未开始,1:已完成,2:进行中,3:挂起,4:测试中", width = 15)
    @ApiModelProperty(value = "执行状态.0:未开始,1:已完成,2:进行中,3:挂起,4:测试中")
    private Integer status;
	/**liked*/
	@Excel(name = "liked", width = 15)
    @ApiModelProperty(value = "liked")
    private Integer liked;
	@TableField(exist = false)
    private String pName;
	@TableField(exist = false)
    private Integer like;
    public Integer getLike(){
        return liked;
    }
    @TableField(exist = false)
    private List<TwTask> childList;

    public Integer getPrivate(){
        return privated;
    }

    @TableField(exist = false)
    private SysUser executor;
    @TableField(exist = false)
    private TwProject projectInfo;
}
