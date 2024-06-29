package org.jeecg.modules.flowable.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.ExcelUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.flowable.domain.dto.FlowTaskDto;
import org.jeecg.modules.flowable.domain.vo.FlowTaskVo;
import org.jeecg.modules.flowable.service.IFlowTaskService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Description: 工作流任务接口
 * @Author: nbacheng
 * @Date:   2022-06-08
 * @Version: V1.0
 */

@Slf4j
@Api(tags = "工作流流程任务接口类")
@RestController
@RequestMapping("/flowable/task")
public class FlowTaskController {

    @Autowired
    private IFlowTaskService flowTaskService;

    @ApiOperation(value = "所有的流程任务", response = FlowTaskDto.class)
    @GetMapping(value = "/allProcess")
    public Result allProcess(@ApiParam(value = "当前页码", required = true) @RequestParam(name="pageNo",defaultValue="1")Integer pageNo,
                                @ApiParam(value = "每页条数", required = true) @RequestParam(name="pageSize",defaultValue="10")Integer pageSize,
                                FlowTaskDto flowTaskDto) {
        return flowTaskService.allProcess(pageNo, pageSize, flowTaskDto);
    }
    
    @ApiOperation(value = "我发起的流程任务", response = FlowTaskDto.class)
    @GetMapping(value = "/myProcess")
    public Result myProcess(@ApiParam(value = "当前页码", required = true) @RequestParam(name="pageNum",defaultValue="1")Integer pageNum,
                                @ApiParam(value = "每页条数", required = true) @RequestParam(name="pageSize",defaultValue="10")Integer pageSize) {
        return flowTaskService.myProcess(pageNum, pageSize);
    }
    
    @ApiOperation(value = "我发起的流程任务", response = FlowTaskDto.class)
    @GetMapping(value = "/myProcessNew")
    public Result myProcessNew(@ApiParam(value = "当前页码", required = true) @RequestParam(name="pageNo",defaultValue="1")Integer pageNo,
                                @ApiParam(value = "每页条数", required = true) @RequestParam(name="pageSize",defaultValue="10")Integer pageSize,
                                FlowTaskDto flowTaskDto) {
        return flowTaskService.myProcessNew(pageNo, pageSize, flowTaskDto);
    }

    @ApiOperation(value = "取消申请", response = FlowTaskDto.class)
    @PostMapping(value = "/stopProcess")
    public Result stopProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.stopProcess(flowTaskVo);
    }

    @ApiOperation(value = "撤回流程", response = FlowTaskDto.class)
    @PostMapping(value = "/revokeProcess")
    public Result revokeProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.revokeProcess(flowTaskVo);
    }
    
    @ApiOperation(value = "收回流程", response = FlowTaskDto.class)
    @PostMapping(value = "/recallProcess")
    public Result recallProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.recallProcess(flowTaskVo);
    }

    @ApiOperation(value = "获取待办列表", response = FlowTaskDto.class)
    @GetMapping(value = "/todoList")
    public Result todoList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                               @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize) {
        return flowTaskService.todoList(pageNum, pageSize);
    }
    
    @ApiOperation(value = "获取待办列表", response = FlowTaskDto.class)
    @GetMapping(value = "/todoListNew")
    public Result todoListNew(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNo,
                               @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                               FlowTaskDto flowTaskDto) {
        return flowTaskService.todoListNew(pageNo, pageSize, flowTaskDto);
    }
    
    @ApiOperation(value = "获取待签列表", response = FlowTaskDto.class)
    @GetMapping(value = "/claimList")
    public Result claimList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNo,
                               @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                               FlowTaskDto flowTaskDto) {
        return flowTaskService.claimList(pageNo, pageSize, flowTaskDto);
    }

    @ApiOperation(value = "获取已办任务", response = FlowTaskDto.class)
    @GetMapping(value = "/finishedList")
    public Result finishedList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum,
                                   @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize) {
        return flowTaskService.finishedList(pageNum, pageSize);
    }

    @ApiOperation(value = "获取已办任务", response = FlowTaskDto.class)
    @GetMapping(value = "/finishedListNew")
    public Result finishedListNew(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNo,
                                   @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize,
                                   FlowTaskDto flowTaskDto) {
        return flowTaskService.finishedListNew(pageNo, pageSize, flowTaskDto);
    }

    @ApiOperation(value = "流程历史流转记录业务dataid", response = FlowTaskDto.class)
    @GetMapping(value = "/flowRecordBydataid")
    public Result flowRecordBydataid(String dataId) {
        return flowTaskService.flowRecordBydataid(dataId);
    }
    
    // add by nbacheng
    @ApiOperation(value = "流程历史流转记录业务", response = FlowTaskDto.class)
    @GetMapping(value = "/flowRecord")
    public Result flowRecord(String procInsId,String deployId, String businessKey, String taskId, String category) {
        return flowTaskService.flowRecord(procInsId,deployId, businessKey, taskId, category);
    }

    @ApiOperation(value = "获取流程变量", response = FlowTaskDto.class)
    @GetMapping(value = "/processVariables/{taskId}")
    public Result processVariables(@ApiParam(value = "流程任务Id")  @PathVariable(value = "taskId") String taskId) {
        return flowTaskService.processVariables(taskId);
    }

    @ApiOperation(value = "审批任务")
    @PostMapping(value = "/complete")
    public Result complete(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.complete(flowTaskVo);
    }


    @ApiOperation(value = "驳回任务")
    @PostMapping(value = "/reject")
    public Result taskReject(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReject(flowTaskVo);
        return Result.OK("驳回任务成功");
    }
    
    @ApiOperation(value = "退回任务")
    @PostMapping(value = "/return")
    public Result taskReturn(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReturn(flowTaskVo);
        return Result.OK("退回任务成功");
    }

    @ApiOperation(value = "获取所有可回退的节点")
    @PostMapping(value = "/returnList")
    public Result findReturnTaskList(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.findReturnTaskList(flowTaskVo);
    }


    @ApiOperation(value = "删除任务")
    @DeleteMapping(value = "/delete")
    public Result delete(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.deleteTask(flowTaskVo);
        return Result.OK("删除任务成功");
    }

    @ApiOperation(value = "认领/签收任务")
    @PostMapping(value = "/claim")
    public Result claim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.claim(flowTaskVo);
        return Result.OK("认领/签收任务成功");
    }

    @ApiOperation(value = "取消认领/签收任务")
    @PostMapping(value = "/unClaim")
    public Result unClaim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.unClaim(flowTaskVo);
        return Result.OK("取消认领/签收任务成功");
    }

    @ApiOperation(value = "委派任务")
    @PostMapping(value = "/delegate")
    public Result delegate(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.delegateTask(flowTaskVo);
        return Result.OK("委派任务成功");
    }

    @ApiOperation(value = "转办任务")
    @PostMapping(value = "/assign")
    public Result assign(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.assignTask(flowTaskVo);
        return Result.OK("转办任务成功");
    }
    
    @ApiOperation(value = "任务跳转")
    @PostMapping(value = "/jump")
    public Result jumpTask(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.jumpTask(flowTaskVo);
        return Result.OK("跳转任务成功");
    }
    
    @ApiOperation(value = "获取用户任务节点,作为跳转任务使用")
    @PostMapping(value = "/userTask")
    public Result userTaskList(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.userTaskList(flowTaskVo);
    }

    @ApiOperation(value = "获取下一节点")
    @PostMapping(value = "/nextFlowNode")
    public Result getNextFlowNode(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.getNextFlowNode(flowTaskVo);
    }
    
    @ApiOperation(value = "获取下一节点的通用同意拒绝排它网关")
    @PostMapping(value = "/nextApprovedEG")
    public Result nextApprovedEG(@RequestParam(name="taskId",required=true) String taskId) {
        return Result.OK(flowTaskService.getNextApprovedExclusiveGateway(taskId));
    }
    
    @ApiOperation(value = "加签任务")
    @PostMapping(value = "/addSign")
    public Result addSignTask(@RequestBody FlowTaskVo taskVo) {
        flowTaskService.addSignTask(taskVo);
        return Result.OK("加签任务成功");
    }
    
    @ApiOperation(value = "多实例加签任务")
    @PostMapping(value = "/multiInstanceAddSign")
    public Result multiInstanceAddSign(@RequestBody FlowTaskVo taskVo) {
        flowTaskService.multiInstanceAddSignTask(taskVo);
        return Result.OK("多实例加签任务成功");
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @RequestMapping("/diagram/{processId}")
    public void genProcessDiagram(HttpServletResponse response,
                                  @PathVariable("processId") String processId) {
        InputStream inputStream =  flowTaskService.diagram(processId);
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成流程图
     *
     * @param procInsId 任务ID
     */
    @RequestMapping("/flowViewer/{procInsId}")
    public Result getFlowViewer(@PathVariable("procInsId") String procInsId) {
        return flowTaskService.getFlowViewer(procInsId);
    }
    /**
     * 生成流程图
     *
     * @param dataId 任务数据ID
     */
    @RequestMapping("/flowViewerByDataId/{dataId}")
    public Result getFlowViewerByDataId(@PathVariable("dataId") String dataId) {
        return flowTaskService.getFlowViewerByDataId(dataId);
    }
    
    /**
     * 生成流程图
     *
     * @param processDefinitionName 任务数据name
     */
    @RequestMapping("/flowViewerByName/{processDefinitionName}")
    public Result getFlowViewerByName(@PathVariable("processDefinitionName") String processDefinitionName) {
        return flowTaskService.getFlowViewerByName(processDefinitionName);
    }
    
    /**
     * 导出excel
     *
     * @param request
     * @param HttpServletResponse response, FlowTaskDto flowTaskDto
     */
     @RequestMapping(value = "/myExportXls")
     public void myExportXls(HttpServletResponse response, FlowTaskDto flowTaskDto) {
    	 
    	  String[] headers = { "任务编号", "流程名称", "流程类别", "流程版本", "业务主键", "提交时间","流程状态","耗时","当前节点","办理"};
    	  String[] columns = { "procInsId","procDefName","category","procDefVersion","businessKey","createTime","finishTime","duration","taskName","assigneeName"};
    	  List<FlowTaskDto> listflowtask = ((Page<FlowTaskDto>)flowTaskService.myProcessNew(1, 10, flowTaskDto).getResult()).getRecords();
    	  ExcelUtils<FlowTaskDto> eu = new ExcelUtils<FlowTaskDto>();
    	  eu.exportExcel(response, "标题", headers, columns, listflowtask, "test.xls", "yyyy-MM-dd HH:mm:ss");//目前这个文件名没有什么用，前端传过来会修改掉
	  }
     /**
      * 导出excel
      *
      * @param request
      * @param HttpServletResponse response, FlowTaskDto flowTaskDto
      */
      @RequestMapping(value = "/allExportXls")
      public void allExportXls(HttpServletResponse response, FlowTaskDto flowTaskDto) {
     	 
     	  String[] headers = { "任务编号", "流程名称", "流程类别", "流程版本", "业务主键", "提交时间","流程状态","耗时","当前节点","办理"};
     	  String[] columns = { "procInsId","procDefName","category","procDefVersion","businessKey","createTime","finishTime","duration","taskName","assigneeName"};
     	  List<FlowTaskDto> listflowtask = ((Page<FlowTaskDto>)flowTaskService.allProcess(1, 10, flowTaskDto).getResult()).getRecords();
     	  ExcelUtils<FlowTaskDto> eu = new ExcelUtils<FlowTaskDto>();
     	  eu.exportExcel(response, "标题", headers, columns, listflowtask, "test.xls", "yyyy-MM-dd HH:mm:ss");//目前这个文件名没有什么用，前端传过来会修改掉
 	   }
      /**
       * 导出excel
       *
       * @param request
       * @param HttpServletResponse response, FlowTaskDto flowTaskDto
       */
       @RequestMapping(value = "/todoExportXls")
       public void todoExportXls(HttpServletResponse response, FlowTaskDto flowTaskDto) {
      	 
      	  String[] headers = { "任务编号", "流程名称", "流程类别", "流程版本", "业务主键", "提交时间","流程状态","耗时","当前节点","办理"};
      	  String[] columns = { "procInsId","procDefName","category","procDefVersion","businessKey","createTime","finishTime","duration","taskName","assigneeName"};
      	  List<FlowTaskDto> listflowtask = ((Page<FlowTaskDto>)flowTaskService.todoListNew(1, 10, flowTaskDto).getResult()).getRecords();
      	  ExcelUtils<FlowTaskDto> eu = new ExcelUtils<FlowTaskDto>();
      	  eu.exportExcel(response, "标题", headers, columns, listflowtask, "test.xls", "yyyy-MM-dd HH:mm:ss");//目前这个文件名没有什么用，前端传过来会修改掉
  	   }
       /**
        * 导出excel
        *
        * @param request
        * @param HttpServletResponse response, FlowTaskDto flowTaskDto
        */
        @RequestMapping(value = "/finishedExportXls")
        public void finishedExportXls(HttpServletResponse response, FlowTaskDto flowTaskDto) {
       	 
       	  String[] headers = { "任务编号", "流程名称", "流程类别", "流程版本", "业务主键", "提交时间","流程状态","耗时","当前节点","办理"};
       	  String[] columns = { "procInsId","procDefName","category","procDefVersion","businessKey","createTime","finishTime","duration","taskName","assigneeName"};
       	  List<FlowTaskDto> listflowtask = ((Page<FlowTaskDto>)flowTaskService.finishedListNew(1, 10, flowTaskDto).getResult()).getRecords();
       	  ExcelUtils<FlowTaskDto> eu = new ExcelUtils<FlowTaskDto>();
       	  eu.exportExcel(response, "标题", headers, columns, listflowtask, "test.xls", "yyyy-MM-dd HH:mm:ss");//目前这个文件名没有什么用，前端传过来会修改掉
   	    }

    /**
     * 自定义流程扩展属性可以用于页面的动态逻辑，判断当前节点是否具有某些权限
     * 如果流程已经结束，或者流程不在当前节点处理人，则返回空 网友peter提供的功能
     *
     * @param procInsId
     * @return
     */
    @ApiOperation(value = "根据procInsId获取节扩展点属性")
    @GetMapping("/getFlowProperties/{procInsId}")
    public Map<String, Object> getFlowProperties(@PathVariable String procInsId) {
    	return flowTaskService.getFlowProperties(procInsId);
    }   
    
    /**
     * 任务前加签 （如果多次加签只能显示第一次前加签的处理人来处理任务）
     * 多个加签人处理完毕任务之后又流到自己这里
     * @param FlowTaskVo 
     * @return
     */
    @ApiOperation(value = "向前加签")
    @PostMapping(value = "/addTasksBefore")
    public Result addTasksBefore(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.delegateTask(flowTaskVo);
        return Result.OK("向前加签成功");
    }
    
    /**
     * 自定义业务使用
     * 判断是否是第一个发起人节点，目前只针对退回，驳回情况进行处理
     * @param dataId, variables 参数
     * @return
     */
    @ApiOperation(value = "是否是第一个发起人节点")
    @PostMapping(value = "/isFirstInitiator/{dataId}")
    public Result isFirstInitiator(@ApiParam(value = "流程业务数据id") @PathVariable(value = "dataId") String dataId,
            @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables) {
        
    	String sRevokeOrReject = (String) variables.get("actStatus");
    	String processInstanceId = (String) variables.get("processInstanceId");
    	return Result.OK(flowTaskService.isFirstInitiator(processInstanceId, sRevokeOrReject));
    }	
    
    /**
     * 自定义业务使用
     *  删除自定义业务任务关联表与流程历史表，以便可以重新发起流程。
     * @param dataId, variables 参数
     * @return
     */
    @ApiOperation(value = "删除自定义业务任务关联表与流程历史表")
    @PostMapping(value = "/deleteActivityAndJoin/{dataId}")
    public Result del(@ApiParam(value = "流程业务数据id") @PathVariable(value = "dataId") String dataId,
            @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables) {
    	String sRevokeOrReject = (String) variables.get("actStatus");
    	String processInstanceId = (String) variables.get("processInstanceId");
    	return Result.OK(flowTaskService.deleteActivityAndJoin(dataId, processInstanceId, sRevokeOrReject));
    }	
}
