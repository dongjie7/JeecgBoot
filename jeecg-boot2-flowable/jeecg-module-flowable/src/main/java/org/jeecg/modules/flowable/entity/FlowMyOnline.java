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
 * @Description: flow_my_online
 * @Author: nbacheng
 * @Date:   2022-11-02
 * @Version: V1.0
 */
@Data
@TableName("flow_my_online")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="flow_my_online对象", description="flow_my_online")
public class FlowMyOnline implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**流程定义key 一个key会有多个版本的id*/
	@Excel(name = "流程定义key 一个key会有多个版本的id", width = 15)
    @ApiModelProperty(value = "流程定义key 一个key会有多个版本的id")
    private String processDefinitionKey;
	/**流程定义id 一个流程定义唯一*/
	@Excel(name = "流程定义id 一个流程定义唯一", width = 15)
    @ApiModelProperty(value = "流程定义id 一个流程定义唯一")
    private String processDefinitionId;
	/**流程业务实例id 一个流程业务唯一，本表中也唯一*/
	@Excel(name = "流程业务实例id 一个流程业务唯一，本表中也唯一", width = 15)
    @ApiModelProperty(value = "流程业务实例id 一个流程业务唯一，本表中也唯一")
    private String processInstanceId;
	/**online流程业务简要描述*/
	@Excel(name = "online流程业务简要描述", width = 15)
    @ApiModelProperty(value = "online流程业务简要描述")
    private String title;
	/**online_id，理论唯一*/
	@Excel(name = "online_id，理论唯一", width = 15)
    @ApiModelProperty(value = "online_id，理论唯一")
    private String onlineId;
	/**online数据表id，理论唯一*/
	@Excel(name = "online数据表id，理论唯一", width = 15)
    @ApiModelProperty(value = "online数据表id，理论唯一")
    private String dataId;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
    @ApiModelProperty(value = "申请人")
    private String proposer;
	/**流程状态说明，有：启动  撤回  驳回  审批中  审批通过  审批异常*/
	@Excel(name = "流程状态说明，有：启动  撤回  驳回  审批中  审批通过  审批异常", width = 15)
    @ApiModelProperty(value = "流程状态说明，有：启动  撤回  驳回  审批中  审批通过  审批异常")
    private String actStatus;
	/**当前的节点定义上的Id,*/
	@Excel(name = "当前的节点定义上的Id,", width = 15)
    @ApiModelProperty(value = "当前的节点定义上的Id,")
    private String taskId;
	/**当前的节点*/
	@Excel(name = "当前的节点", width = 15)
    @ApiModelProperty(value = "当前的节点")
    private String taskName;
	/**当前的节点实例上的Id*/
	@Excel(name = "当前的节点实例上的Id", width = 15)
    @ApiModelProperty(value = "当前的节点实例上的Id")
    private String taskNameId;
	/**当前的节点可以处理的用户名*/
	@Excel(name = "当前的节点可以处理的用户名", width = 15)
    @ApiModelProperty(value = "当前的节点可以处理的用户名")
    private String todoUsers;
	/**处理过的人*/
	@Excel(name = "处理过的人", width = 15)
    @ApiModelProperty(value = "处理过的人")
    private String doneUsers;
	/**当前任务节点的优先级 流程定义的时候所填*/
	@Excel(name = "当前任务节点的优先级 流程定义的时候所填", width = 15)
    @ApiModelProperty(value = "当前任务节点的优先级 流程定义的时候所填")
    private String priority;
}
