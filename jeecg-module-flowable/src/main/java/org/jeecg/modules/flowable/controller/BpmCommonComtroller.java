package org.jeecg.modules.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.domain.dto.FlowTaskDto;
import org.jeecg.modules.flowable.entity.BpmToolTableBpmn;
import org.jeecg.modules.flowable.entity.SysDictItem;
import org.jeecg.modules.flowable.service.IFlowTaskService;
import org.jeecg.modules.flowable.service.ISysFormService;
import org.jeecg.modules.flowable.service.impl.BpmToolTableBpmnInfoServiceImpl;
import org.jeecg.modules.flowable.service.impl.HanDongYZCommonServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-09-18 11:57
 * @description: 新增接口Controller
 **/


@Api(tags="2022-9月新增接口-hanDongYuZhou")
@RestController
@RequestMapping("/flowable/bpmCommon")
@Slf4j
public class BpmCommonComtroller {

    @Autowired
    private ISysFormService sysFormService;

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    @Autowired
    private HanDongYZCommonServiceImpl hanDongYZCommonService;

    @Autowired
    private BpmToolTableBpmnInfoServiceImpl bpmToolTableBpmnInfoService;
    @Autowired
    private IFlowTaskService flowTaskService;

    @Autowired
    private IOnlineService onlineService;


    @ApiOperation(value = "发布当前流程配置")
    @PutMapping(value = "/updatePublishData")
    public Result updatePublishData(@ApiParam(value = "流程id", required = true) @RequestParam String bpmId) {

        QueryWrapper<BpmToolTableBpmn> bpmToolTableBpmnInfoQueryWrapper = new QueryWrapper<>();
        bpmToolTableBpmnInfoQueryWrapper.eq("bpm_id",bpmId);
        List<BpmToolTableBpmn> listData = bpmToolTableBpmnInfoService.list(bpmToolTableBpmnInfoQueryWrapper);
        if(listData.size()>0){
            BpmToolTableBpmn bpmToolTableBpmnInfo = listData.get(0);
            bpmToolTableBpmnInfo.setPublishState("2");
            bpmToolTableBpmnInfo.setUpdateTime(new Date());
            bpmToolTableBpmnInfoService.updateById(bpmToolTableBpmnInfo);
        }else{
            return Result.error("未找到当前流程数据信息",null);
        }
        return Result.OK("操作成功");
    }

    @ApiOperation(value = "下线当前流程配置")
    @PutMapping(value = "/updatePublishDataInfoDown")
    public Result publishDataInfoDown(@ApiParam(value = "流程id", required = true) @RequestParam String bpmId) {

        QueryWrapper<BpmToolTableBpmn> bpmToolTableBpmnInfoQueryWrapper = new QueryWrapper<>();
        bpmToolTableBpmnInfoQueryWrapper.eq("bpm_id",bpmId);
        List<BpmToolTableBpmn> listData = bpmToolTableBpmnInfoService.list(bpmToolTableBpmnInfoQueryWrapper);
        if(listData.size()>0){
            BpmToolTableBpmn bpmToolTableBpmnInfo = listData.get(0);
            bpmToolTableBpmnInfo.setPublishState("1");
            bpmToolTableBpmnInfo.setUpdateTime(new Date());
            bpmToolTableBpmnInfoService.updateById(bpmToolTableBpmnInfo);
        }else{
            return Result.error("未找到当前流程数据信息",null);
        }
        return Result.OK("操作成功");
    }

    @ApiOperation(value = "表单类型信息列表")
    @GetMapping("/tableCateList")
    public Result tableCateList(SysCategory category) {
        List<SysCategory> list = new ArrayList<>();
        List<SysDictItem> bpmData = hanDongYZCommonService.allSysDictItemInfo("bpm_table_cate");
        if(bpmData.size()>0){
            for(int a=0;a<bpmData.size();a++){
                SysCategory sysCategory = new SysCategory();
                sysCategory.setId(String.valueOf(bpmData.get(a).getItemValue()));
                sysCategory.setName(String.valueOf(bpmData.get(a).getItemText()));
                list.add(sysCategory);
            }
        }
        return Result.OK(list);
    }


    @ApiOperation(value = "表单类型信息列表-未带未配置项")
    @GetMapping("/tableCateNoList")
    public Result tableCateNoList(SysCategory category) {
        List<SysCategory> list = new ArrayList<>();
        List<SysDictItem> bpmData = hanDongYZCommonService.allSysDictItemInfo("bpm_table_cate");
        if(bpmData.size()>0){
            for(int a=0;a<bpmData.size();a++){
                if(!String.valueOf(bpmData.get(a).getItemValue()).equals("0")){
                    SysCategory sysCategory = new SysCategory();
                    sysCategory.setId(String.valueOf(bpmData.get(a).getItemValue()));
                    sysCategory.setName(String.valueOf(bpmData.get(a).getItemText()));
                    list.add(sysCategory);
                }
            }
        }
        return Result.OK(list);
    }


    @ApiOperation(value = "流程是否发布状态")
    @GetMapping("/publishStatesList")
    public Result publishStatesList(SysCategory category) {
        List<SysCategory> list = new ArrayList<>();
        List<SysDictItem> bpmData = hanDongYZCommonService.allSysDictItemInfo("bpm_publish_state");
        if(bpmData.size()>0){
            for(int a=0;a<bpmData.size();a++){
                if(!String.valueOf(bpmData.get(a).getItemValue()).equals("0")){
                    SysCategory sysCategory = new SysCategory();
                    sysCategory.setId(String.valueOf(bpmData.get(a).getItemValue()));
                    sysCategory.setName(String.valueOf(bpmData.get(a).getItemText()));
                    list.add(sysCategory);
                }
            }
        }
        return Result.OK(list);
    }

    @ApiOperation(value = "查询所有在在线表单数据信息")
    @GetMapping("/onlineTableList")
    public Result onlineTableList(SysCategory category) {
        List<SysCategory> list = new ArrayList<>();
        List<SysDictItem> bpmData = hanDongYZCommonService.allSysDictItemInfo("bpm_process_type");
        if(bpmData.size()>0){
            for(int a=0;a<bpmData.size();a++){
                SysCategory sysCategory = new SysCategory();
                sysCategory.setId(String.valueOf(bpmData.get(a).getItemValue()));
                sysCategory.setName(String.valueOf(bpmData.get(a).getItemText()));
                list.add(sysCategory);
            }
        }
        return Result.OK(list);
    }




    @AutoLog(value = "流程表单配置-在线表单-根据选中的表单查询当前表单的字段信息")
    @ApiOperation(value="流程表单配置-在线表单-根据选中的表单查询当前表单的字段信息", notes="流程表单配置-在线表单-根据选中的表单查询当前表单的字段信息")
    @GetMapping(value = "/onlTableAllFieldList")
    public Result<List<OnlCgformField>> onlTableAllFieldList(@RequestParam(name="tableId",required=true) String tableId, HttpServletRequest req) {
        List<OnlCgformField> resultData = new ArrayList<>();
        QueryWrapper<OnlCgformField> onlCgformFieldQueryWrapper = new QueryWrapper<>();
        onlCgformFieldQueryWrapper.eq("cgform_head_id",tableId);
        onlCgformFieldQueryWrapper.orderByDesc("create_time");
        List<OnlCgformField> resultDataInfo = onlCgformFieldService.list(onlCgformFieldQueryWrapper);
        for(int cc=0;cc<resultDataInfo.size();cc++){
            if(!String.valueOf(resultDataInfo.get(cc).getDbFieldName()).equals("id") &&
                    !String.valueOf(resultDataInfo.get(cc).getDbFieldName()).equals("create_by") &&
                    !String.valueOf(resultDataInfo.get(cc).getDbFieldName()).equals("update_by") &&
                    !String.valueOf(resultDataInfo.get(cc).getDbFieldName()).equals("update_time") &&
                    !String.valueOf(resultDataInfo.get(cc).getDbFieldName()).equals("sys_org_code") &&
                    !String.valueOf(resultDataInfo.get(cc).getDbFieldName()).equals("bpm_status")){
                resultData.add(resultDataInfo.get(cc));
            }
        }
        return Result.OK(resultData);
    }

    @AutoLog(value = "流程表单配置-在线表单-根据选中的Online表查询当前表的流程字段数据信息")
    @ApiOperation(value="流程表单配置-在线表单-根据选中的Online表查询当前表的流程字段数据信息", notes="流程表单配置-在线表单-根据选中的Online表查询当前表的流程字段数据信息")
    @GetMapping(value = "/listOnlineTableFieldChooseWyz")
    public Result<OnlCgformField> listOnlineTableFieldChooseWyz(@RequestParam(name="tableId",required=true) String tableId,HttpServletRequest req) {
        OnlCgformField resultData = new OnlCgformField();

        QueryWrapper<OnlCgformField> onlCgformFieldQueryWrapper = new QueryWrapper<>();
        onlCgformFieldQueryWrapper.eq("cgform_head_id",tableId);
        onlCgformFieldQueryWrapper.orderByDesc("create_time");
        List<OnlCgformField> resultFromField = onlCgformFieldService.list(onlCgformFieldQueryWrapper);
        if(resultFromField.size()>0){
            for(int a=0;a<resultFromField.size();a++){
                if(String.valueOf(resultFromField.get(a).getDbFieldName()).equals("bpm_status")){
                    resultData = resultFromField.get(a);
                }
            }
        }
        return Result.OK(resultData);
    }



    @ApiOperation(value = "流程历史流转记录业务-WangYuZhou", response = FlowTaskDto.class)
    @GetMapping(value = "/flowRecordNew")
    public Result flowRecordNew(String procInsId,String deployId, String procDefId) {
        //return flowTaskService.flowRecordNew(procInsId,deployId, procDefId);
    	return Result.OK(null);
    }

}
