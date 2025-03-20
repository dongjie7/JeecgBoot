package org.jeecg.modules.flowable.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 流程实例关联表单
 * @Author: nbacheng
 * @Date:   2022-04-11
 * @Version: V1.0
 */
@Data
@TableName("sys_deploy_form")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_deploy_form对象", description="流程实例关联表单")
public class SysDeployForm implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**表单主键*/
	@Excel(name = "表单主键", width = 15)
    @ApiModelProperty(value = "表单主键")
    private String formId;
	/**流程实例主键*/
	@Excel(name = "流程实例主键", width = 15)
    @ApiModelProperty(value = "流程实例主键")
    private String deployId;
	/**流程实例节点主键*/
	@Excel(name = "流程实例节点主键", width = 15)
    @ApiModelProperty(value = "流程实例节点主键")
    private String nodeKey;
	/**流程实例节点名称*/
	@Excel(name = "流程实例节点名称", width = 15)
    @ApiModelProperty(value = "流程实例节点名称")
    private String nodeName;
	/**流程实例节点名称*/
	@Excel(name = "流程实例节点form标志", width = 15)
    @ApiModelProperty(value = "流程实例节点form标志")
    private String formFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
