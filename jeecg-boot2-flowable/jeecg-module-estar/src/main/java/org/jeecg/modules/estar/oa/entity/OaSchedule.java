package org.jeecg.modules.estar.oa.entity;

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
 * @Description: OA日程表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Data
@TableName("oa_schedule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="oa_schedule对象", description="OA日程表")
public class OaSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private String title;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private String content;
	/**日历类型*/
	@Excel(name = "日历类型", width = 15)
    @ApiModelProperty(value = "日历类型")
    private String calId;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
	/**是否全天*/
	@Excel(name = "是否全天", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否全天")
    private Integer allday;
	/**所属人*/
	@Excel(name = "所属人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "所属人")
    private String owner;
	/**参与人*/
	@Excel(name = "参与人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "参与人")
    private String taker;
	/**优先级*/
	@Excel(name = "优先级", width = 15, dicCode = "oa_reminder_level")
	@Dict(dicCode = "oa_reminder_level")
    @ApiModelProperty(value = "优先级")
    private Integer level;
	/**提前提醒时间*/
	@Excel(name = "提前提醒时间", width = 15, dicCode = "oa_reminder_time")
	@Dict(dicCode = "oa_reminder_time")
    @ApiModelProperty(value = "提前提醒时间")
    private Integer remind;
	/**提醒方式*/
	@Excel(name = "提醒方式", width = 15, dicCode = "oa_reminder_type")
	@Dict(dicCode = "oa_reminder_type")
    @ApiModelProperty(value = "提醒方式")
    private String remindType;
	/**地点*/
	@Excel(name = "地点", width = 15)
    @ApiModelProperty(value = "地点")
    private String address;
	/**颜色*/
	@Excel(name = "颜色", width = 15)
    @ApiModelProperty(value = "颜色")
    private String color;
	/**来源*/
	@Excel(name = "来源", width = 15)
    @ApiModelProperty(value = "来源")
    private String source;
	/**链接*/
	@Excel(name = "链接", width = 15)
    @ApiModelProperty(value = "链接")
    private String url;
	/**发送消息ID*/
	@Excel(name = "发送消息ID", width = 15)
    @ApiModelProperty(value = "发送消息ID")
    private String msgId;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
