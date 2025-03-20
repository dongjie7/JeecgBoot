package org.jeecg.modules.flowable.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>工作流任务<p>
 *
 */
//@Getter
//@Setter
@Data
@ApiModel("工作流任务相关-返回参数")
public class FlowTaskDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("任务编号")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务Key")
    private String taskDefKey;

    @ApiModelProperty("任务执行人Id")
    private String assigneeId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("流程发起人部门名称")
    private String startDeptName;

    @ApiModelProperty("任务执行人名称")
    private String assigneeName;

    @ApiModelProperty("流程发起人Id")
    private String startUserId;

    @ApiModelProperty("流程发起人名称")
    private String startUserName;

    @ApiModelProperty("流程类型")
    private String category;

    @ApiModelProperty("流程变量信息")
    private Object procVars;

    @ApiModelProperty("局部变量信息")
    private Object taskLocalVars;

    @ApiModelProperty("流程部署编号")
    private String deployId;

    @ApiModelProperty("流程ID")
    private String procDefId;

    @ApiModelProperty("流程key")
    private String procDefKey;

    @ApiModelProperty("流程定义名称")
    private String procDefName;

    @ApiModelProperty("流程定义内置使用版本")
    private int procDefVersion;

    @ApiModelProperty("流程实例ID")
    private String procInsId;

    @ApiModelProperty("历史流程实例ID")
    private String hisProcInsId;

    @ApiModelProperty("任务耗时")
    private String duration;

    @ApiModelProperty("任务意见")
    //private FlowCommentDto comment;
    private List<FlowCommentDto> listFlowCommentDto;

    @ApiModelProperty("意见附件")
    private List<FlowCommentFileDto> listcommentFileDto;
    
    @ApiModelProperty("候选执行人")
    private String candidate;

    @ApiModelProperty("任务创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //返回时间类型
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") //接收时间类型  支持前端字符串日期传入
    private Date createTime;

    @ApiModelProperty("任务完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date finishTime;
  
    @ApiModelProperty("自定义业务主键")   //add by nbahcneg
    private String businessKey;
    
    @ApiModelProperty("任务当前节点类型")   // 目前只对多实例会签进行处理   add by nbahcneg 
    private String NodeType;
    
    @ApiModelProperty("流程表单变量信息")
    private Map<String, Object> taskFormValues;// 历史任务表单参数变量，目前主要针对节点多表单处理   add by nbahcneg
    
    @ApiModelProperty("活动类型")
    private String activityType; //主要是前端显示开始结束流程使用
}
