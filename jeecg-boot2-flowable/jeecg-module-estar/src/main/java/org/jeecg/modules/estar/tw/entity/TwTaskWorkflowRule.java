package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 任务工作流规则表
 * @Author: nbacheng
 * @Date:   2023-06-26
 * @Version: V1.0
 */
@Data
@TableName("tw_task_workflow_rule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_task_workflow_rule对象", description="任务工作流规则表")
public class TwTaskWorkflowRule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**规则类型，0：任务分组，1：人员，2：条件*/
	@Excel(name = "规则类型，0：任务分组，1：人员，2：条件", width = 15)
    @ApiModelProperty(value = "规则类型，0：任务分组，1：人员，2：条件")
    private Integer type;
	/**对象ID*/
	@Excel(name = "对象ID", width = 15)
    @ApiModelProperty(value = "对象ID")
    private String objectId;
	/**场景。0：任何条件，1：被完成，2：被重做，3：设置执行人，4：截止时间，5：优先级*/
	@Excel(name = "场景。0：任何条件，1：被完成，2：被重做，3：设置执行人，4：截止时间，5：优先级", width = 15)
    @ApiModelProperty(value = "场景。0：任何条件，1：被完成，2：被重做，3：设置执行人，4：截止时间，5：优先级")
    private Integer action;
	/**工作流id*/
	@Excel(name = "工作流id", width = 15)
    @ApiModelProperty(value = "工作流id")
    private String workflowId;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
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
