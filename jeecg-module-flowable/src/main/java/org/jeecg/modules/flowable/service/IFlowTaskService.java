package org.jeecg.modules.flowable.service;

import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.flowable.domain.dto.FlowTaskDto;
import org.jeecg.modules.flowable.domain.vo.FlowTaskVo;
import org.jeecg.modules.flowable.entity.ExtensionElementInfo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 工作流任务服务类
 * @Author: nbacheng
 * @Date:   2022-06-08
 * @Version: V1.0
 */

public interface IFlowTaskService {

    /**
     * 审批任务
     *
     * @param task 请求实体参数
     */
    Result complete(FlowTaskVo task);
    
    /**
     * 审批任务
     *
     * @param task 请求实体参数 ，有业务dataid
     */
	//Result completeForDataID(FlowTaskVo taskVo); 

    /**
     * 驳回任务
     *
     * @param flowTaskVo
     */
    void taskReject(FlowTaskVo flowTaskVo);
    /**
     * 驳回任务 for自定义业务
     *
     * @param flowTaskVo 请求业务DataId
     */
	void taskRejectForDataId(FlowTaskVo flowTaskVo);

    /**
     * 退回任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void taskReturn(FlowTaskVo flowTaskVo);
    /**
     * 退回任务
     *
     * @param flowTaskVo 请求实体参数 请求业务DataId
     */
	void taskReturnForDataId(FlowTaskVo flowTaskVo);

    /**
     * 获取所有可回退的节点
     *
     * @param flowTaskVo
     * @return
     */
    Result findReturnTaskList(FlowTaskVo flowTaskVo);


    /**
     * 删除任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void deleteTask(FlowTaskVo flowTaskVo);

    /**
     * 认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void claim(FlowTaskVo flowTaskVo);

    /**
     * 取消认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void unClaim(FlowTaskVo flowTaskVo);

    /**
     * 委派任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void delegateTask(FlowTaskVo flowTaskVo);


    /**
     * 转办任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void assignTask(FlowTaskVo flowTaskVo);

    /**
     * 所有流程任务
     * @param pageNum
     * @param pageSize
     * @param flowTaskDto
     * @return
     */
	Result allProcess(Integer pageNo, Integer pageSize, FlowTaskDto flowTaskDto);
    
    /**
     * 我发起的流程
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result myProcess(Integer pageNum, Integer pageSize);

    /**
     * 我发起的流程
     * @param pageNo
     * @param pageSize
     * @return
     */
	Result myProcessNew(Integer pageNo, Integer pageSize, FlowTaskDto flowTaskDto);
	
    /**
     * 取消申请
     * @param flowTaskVo
     * @return
     */
    Result stopProcess(FlowTaskVo flowTaskVo);

    /**
     * 撤回流程
     * @param flowTaskVo
     * @return
     */
    Result revokeProcess(FlowTaskVo flowTaskVo);
    
    Result revokeProcessForDataId(FlowTaskVo flowTaskVo);


    /**
     * 代办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    Result todoList(Integer pageNum, Integer pageSize);

	Result todoListNew(Integer pageNo, Integer pageSize, FlowTaskDto flowTaskDto);

	/**
     * 代签任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */

	Result claimList(Integer pageNo, Integer pageSize, FlowTaskDto flowTaskDto);
	
    /**
     * 已办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    Result finishedList(Integer pageNum, Integer pageSize);

	Result finishedListNew(Integer pageNum, Integer pageSize, FlowTaskDto flowTaskDto);
    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例Id, 流程发布id,业务id, 任务Id, 流程类型
     * @return
     */
    Result flowRecord(String procInsId,String deployId, String businessKey,String taskId, String category);

    /**
     * 流程历史流转记录
     *
     * @param 业务dataId
     * @return
     */
	Result flowRecordBydataid(String dataId);
    
    /**
     * 根据任务ID查询挂载的表单信息
     *
     * @param taskId 任务Id
     * @return
     */
    Task getTaskForm(String taskId);

    /**
     * 获取流程过程图
     * @param processId
     * @return
     */
    InputStream diagram(String processId);

    /**
     * 获取流程执行过程
     * @param procInsId
     * @return
     */
    Result getFlowViewer(String procInsId);
    Result getFlowViewerByDataId(String dataId);
    Result getFlowViewerByName(String processDefinitionName); //add by nbacheng
    
    /**
     * 获取流程变量
     * @param taskId
     * @return
     */
    Result processVariables(String taskId);

    /**
     * 获取下一节点
     * @param flowTaskVo 任务
     * @return
     */
    Result getNextFlowNode(FlowTaskVo flowTaskVo);

    /**
     * 流程历史当前记录信息
     * add by nbacheng
     * @param  procInsId 流程实例Id
     * @return
     */
    Map<String, Object> currentFlowRecord(String procInsId);
    
    /**
	 * 获取下个节点信息,对排它网关的通用同意拒绝做特殊处理
	 *  add by nbacheng
	 *           
	 * @param FlowTaskVo taskVo
	 *           
	 * @return
	 */
    boolean getNextApprovedExclusiveGateway(String taskId);

	Map<String, Object> getFlowProperties(String procInsId);

	/**
     * 收回流程,收回后发起人可以重新编辑表单发起流程，对于自定义业务就是原有任务都删除，重新进行申请
     * @param flowTaskVo
     * @return
     */
	Result recallProcess(FlowTaskVo flowTaskVo);
	
	/**
     * 任务前加签 （如果多次加签只能显示第一次前加签的处理人来处理任务）
     * 多个加签人处理完毕任务之后又流到自己这里
     *
     * @param processInstanceId 流程实例id
     * @param assignee          受让人
     * @param description       描述
     * @param assignees 被加签人
     */
    void addTasksBefore(FlowTaskVo flowTaskVo, TaskEntityImpl taskEntity, String assignee, Set<String> assignees, String description);

    /**
     * 任务后加签（加签人自己自动审批完毕加签多个人处理任务）
     *
     * @param processInstanceId 流程实例id
     * @param assignee          受让人
     * @param description       描述
     * @param assignees 被加签人
     */
    void addTasksAfter(FlowTaskVo flowTaskVo, TaskEntityImpl taskEntity, String assignee, Set<String> assignees, String description);

    /**
     * 添加任务
     *
     * @param processInstanceId 流程实例id
     * @param assignee          受让人
     * @param description       描述
     * @param assignees 被加签人
     * @param flag  true向后加签  false向前加签
     */
    void addTask(FlowTaskVo flowTaskVo, TaskEntityImpl taskEntity, String assignee, Set<String> assignees, String description, Boolean flag);

    /**
     * 获取序列流扩展节点
     *
     * @param taskId 任务ID
     * @return 
     */
    Map<String, List<ExtensionElement>> getSequenceFlowExtensionElement(String taskId);

    /**
     * 获取扩展属性值
     *
     * @param taskId 任务ID
     * @return 
     */
    List<ExtensionElementInfo> getExtensionElement(String taskId);

    /**
     * 加签任务
     *
     * @param FlowTaskVo
     * @return 
     */
	void addSignTask(FlowTaskVo flowTaskVo);

	
	/**
     * 多实例加签任务
     *
     * @param FlowTaskVo
     * @return 
     */
	void multiInstanceAddSignTask(FlowTaskVo flowTaskVo);

	/**
     * 跳转任务
     *
     * @param FlowTaskVo
     * @return 
     */
	void jumpTask(FlowTaskVo flowTaskVo);

	/**
     * 用户任务列表,作为跳转任务使用
     *
     * @param FlowTaskVo
     * @return 
     */
	Result userTaskList(FlowTaskVo flowTaskVo);

	/**
	* 判断当前节点是否是第一个发起人节点(目前只针对驳回、撤回和退回操作)
	*
	* @param processInstanceId,  actStatusType
	*/
	boolean isFirstInitiator(String processInstanceId, String actStatusType);
	/**
     * 自定义业务使用
     *  删除自定义业务任务关联表与流程历史表，以便可以重新发起流程。
     * @param dataId, variables 参数
     * @return
     */

	boolean deleteActivityAndJoin(String dataId, String processInstanceId, String actStatusType);
}
