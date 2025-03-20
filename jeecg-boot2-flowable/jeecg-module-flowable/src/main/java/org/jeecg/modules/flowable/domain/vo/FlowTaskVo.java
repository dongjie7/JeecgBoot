package org.jeecg.modules.flowable.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.flowable.domain.dto.FlowCommentFileDto;

/**
 *  流程任务
 *  nbacheng
 */
@Data
@ApiModel("工作流任务相关--请求参数")
public class FlowTaskVo {

    @ApiModelProperty("数据Id")
    private String dataId;
    @ApiModelProperty("任务Id")
    private String taskId;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("任务意见")
    private String comment;
    
    @ApiModelProperty("任务附件")
    private FlowCommentFileDto commentFileDto;

    @ApiModelProperty("流程实例Id")
    private String instanceId;

    @ApiModelProperty("节点")
    private String targetKey;

    @ApiModelProperty("流程变量信息")
    private Map<String, Object> values;

    @ApiModelProperty("审批人")
    private String assignee;

    @ApiModelProperty("候选人")
    private List<String> candidateUsers;

    @ApiModelProperty("审批组")
    private List<String> candidateGroups;
    
    @ApiModelProperty("自定义业务主键")  
    private String businessKey;
    
    @ApiModelProperty("抄送人")
    private String ccUsers;
    
    @ApiModelProperty("审批时人工指定下一审批人")
    private String nextUsers;
    
    @ApiModelProperty("流程类型")
    private String category;
    
    @ApiModelProperty("第一用户发起节点的变量")
    private Map<String, Object> variables;//主要为退回到第一个用户任务节点的时候进行表单编辑用
    
    @ApiModelProperty("下一个网关是否是同意拒绝通用网关")
    private boolean approved; //网关条件是${approved}或${!approved}
    
    //加签相关数据
    @ApiModelProperty("加签用户")
    private String addSignUsers;
    
    @ApiModelProperty("加签类型")
    private String addSignType; //前加签0，后加签1，多实例加签2
    
    @ApiModelProperty("目标活动节点id，当跳转或退回或加签减签时使用")
    private String targetActId;
    
    @ApiModelProperty("目标活动节点名称，当跳转或退回或加签减签时使用")
    private String targetActName;
}
