package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 项目日志表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Data
@TableName("tw_project_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_project_log对象", description="项目日志表")
public class TwProjectLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**操作人id*/
	@Excel(name = "操作人id", width = 15)
    @ApiModelProperty(value = "操作人id")
    private String memberId;
	/**操作内容*/
	@Excel(name = "操作内容", width = 15)
    @ApiModelProperty(value = "操作内容")
    private String content;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private String remark;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    private String opeType;
	/**添加时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private String sourceId;
	/**场景类型*/
	@Excel(name = "场景类型", width = 15)
    @ApiModelProperty(value = "场景类型")
    private String actionType;
	/**toMemberId*/
	@Excel(name = "toMemberId", width = 15)
    @ApiModelProperty(value = "toMemberId")
    private String toMemberId;
	/**是否评论，0：否*/
	@Excel(name = "是否评论，0：否", width = 15)
    @ApiModelProperty(value = "是否评论，0：否")
    private Integer isComment;
	/**projectId*/
	@Excel(name = "projectId", width = 15)
    @ApiModelProperty(value = "projectId")
    private String projectId;
	/**icon*/
	@Excel(name = "icon", width = 15)
    @ApiModelProperty(value = "icon")
    private String icon;
	/**是否机器人*/
	@Excel(name = "是否机器人", width = 15)
    @ApiModelProperty(value = "是否机器人")
    private Integer isRobot;
	
	@TableField(exist = false)
    private SysUser member;
}
