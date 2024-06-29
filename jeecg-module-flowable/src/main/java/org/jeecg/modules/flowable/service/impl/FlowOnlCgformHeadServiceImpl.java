package org.jeecg.modules.flowable.service.impl;

import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.entity.BpmToolDesigner;
import org.jeecg.modules.flowable.entity.BpmToolRule;
import org.jeecg.modules.flowable.entity.FlowOnlCgformHead;
import org.jeecg.modules.flowable.entity.SysDictItem;
import org.jeecg.modules.flowable.entity.vo.LinkDownCateCode;
import org.jeecg.modules.flowable.entity.vo.LinkDownDataVO;
import org.jeecg.modules.flowable.entity.vo.OnlCgformDataVo;
import org.jeecg.modules.flowable.entity.vo.OnlCgformFieldRuleVO;
import org.jeecg.modules.flowable.entity.vo.OnlCgformFieldVO;
import org.jeecg.modules.flowable.entity.vo.OnlCgformHeadVO;
import org.jeecg.modules.flowable.entity.vo.SwitchDataInfo;
import org.jeecg.modules.flowable.entity.vo.SysCateDictCode;
import org.jeecg.modules.flowable.listener.MutiInstanceExecutionListener;
import org.jeecg.modules.flowable.mapper.FlowOnlCgformHeadMapper;
import org.jeecg.modules.flowable.service.IFlowOnlCgformHeadService;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformSqlService;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: onl_cgform_head
 * @Author: nbacheng 参考网友WangYuZhou提供的代码修改
 * @Date:   2022-10-22
 * @Version: V1.0
 */
@Slf4j
@Service
public class FlowOnlCgformHeadServiceImpl extends ServiceImpl<FlowOnlCgformHeadMapper, FlowOnlCgformHead> implements IFlowOnlCgformHeadService {

	@Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;
    
    @Autowired
    private HanDongYZCommonServiceImpl hanDongYZCommonService;
    
    @Autowired
    private BpmToolDesignerServiceImpl bpmToolDesignerService;

    @Autowired
    private BpmToolRuleServiceImpl bpmToolRuleService;
    
    @Autowired
    IOnlCgformSqlService onlCgformSqlService;
	
	@Override
	public Map<String, Object> getOnlCgformHeadByFormId(String formId) {
		// TODO Auto-generated method stub
		 Map<String, Object> map = new HashMap<String, Object>();
		 List<OnlCgformHeadVO> onlData  = new ArrayList<OnlCgformHeadVO>();
         OnlCgformHead onlCgFormHead = onlCgformHeadService.getById(formId);
         if(String.valueOf(onlCgFormHead.getTableType()).equals("2")){//表类型: 1单表、2主表、3附表
             String.valueOf(onlCgFormHead.getTableType());

             //查询当前表的附表
             String[]  attInfo = String.valueOf(onlCgFormHead.getSubTableStr()).split(",");
             ArrayList<String> arrayList = new ArrayList<String>(attInfo.length);
             Collections.addAll(arrayList, attInfo);
             //查询
             QueryWrapper<OnlCgformHead> onlCgformHeadQueryWrapper = new QueryWrapper<OnlCgformHead>();
             onlCgformHeadQueryWrapper.eq("table_type",Integer.valueOf(3));
             onlCgformHeadQueryWrapper.eq("is_db_synch","Y");
             onlCgformHeadQueryWrapper.in("table_name",arrayList);
             onlCgformHeadQueryWrapper.orderByAsc("tab_order_num");
             List<OnlCgformHead>  onlCgformHeadAll = onlCgformHeadService.list(onlCgformHeadQueryWrapper);
             //整理返回数据信息
             onlData.add(dataInfomation(onlCgFormHead));

             for(int i=0;i<onlCgformHeadAll.size();i++){
                 onlData.add(dataInfomation(onlCgformHeadAll.get(i)));
             }
         }else{

             onlData.add(dataInfomation(onlCgFormHead));
         }
         map.put("formData", onlData);
		return map;
	}
	
    private OnlCgformHeadVO dataInfomation(OnlCgformHead onlCgformHead){
        OnlCgformHeadVO onlCgformHeadVO = new OnlCgformHeadVO();
        if(String.valueOf(onlCgformHead.getTableType()).equals("1")){
            onlCgformHeadVO.setOnlTitleName(String.valueOf(onlCgformHead.getTableTxt()));
        }else if(String.valueOf(onlCgformHead.getTableType()).equals("2")){
            onlCgformHeadVO.setOnlTitleName("(主)"+String.valueOf(onlCgformHead.getTableTxt()));
        }else if(String.valueOf(onlCgformHead.getTableType()).equals("3")){
            onlCgformHeadVO.setOnlTitleName("(附)"+String.valueOf(onlCgformHead.getTableTxt()));
        }
        //根据表单id查询当前表字段数据信息
        List<OnlCgformFieldVO>   fieldInfo =  new ArrayList<OnlCgformFieldVO>();
        QueryWrapper<OnlCgformField> onlCgformFieldQueryWrapper = new QueryWrapper<>();
        onlCgformFieldQueryWrapper.eq("cgform_head_id",String.valueOf(onlCgformHead.getId()));
        onlCgformFieldQueryWrapper.ne("main_table",String.valueOf(onlCgformHead.getTableName()));
        onlCgformFieldQueryWrapper.orderByAsc("order_num");
        List<OnlCgformField> resultFromFieldInfo = onlCgformFieldService.list(onlCgformFieldQueryWrapper);
        if(resultFromFieldInfo.size()>0){
            //去除不必要的数据字段信息
            for(int cc=0;cc<resultFromFieldInfo.size();cc++){
                if(!String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("id") &&
                        !String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("create_by") &&
                        !String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("create_time") &&
                        !String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("update_by") &&
                        !String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("update_time") &&
                        !String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("sys_org_code") &&
                        !String.valueOf(resultFromFieldInfo.get(cc).getDbFieldName()).equals("bpm_status")){
                    fieldInfo.add(backFieldDataInfo(resultFromFieldInfo.get(cc)));
                }
            }
            //整理数据信息-根据字段控件类型替换linkDown组件其他字段下拉框数据信息
            String otherFieldInfomation ="";
            for(int qq=0;qq<fieldInfo.size();qq++){
                if(String.valueOf(fieldInfo.get(qq).getLinkDowmInfomation()).length()>0 && !String.valueOf(fieldInfo.get(qq).getLinkDowmInfomation()).equals("null")){
                    otherFieldInfomation = String.valueOf(fieldInfo.get(qq).getLinkDowmInfomation());
                }
            }
            String[]  otherFieldInfo= otherFieldInfomation.split(",");
            //log.info(String.valueOf(onlCgformHead.getFormTemplate()));
            Integer cutomLie = Integer.valueOf(24)/Integer.valueOf(String.valueOf(onlCgformHead.getFormTemplate()));
            //log.info(String.valueOf(cutomLie));
            if(otherFieldInfo.length>0){
                for(int mm=0;mm<fieldInfo.size();mm++){
                    for(int nn=0;nn<otherFieldInfo.length;nn++){
                        fieldInfo.get(mm).setFieldDataTopInfo(topDataInfo(fieldInfo.get(mm).getTableTypeCode(),cutomLie));
                        //log.info(String.valueOf(fieldInfo.get(mm).getDbFieldName()));
                        //log.info(String.valueOf(otherFieldInfo[nn]));
                        if(String.valueOf(fieldInfo.get(mm).getDbFieldName()).equals(String.valueOf(otherFieldInfo[nn]))){
                            fieldInfo.get(mm).setTableTypeCode("hanDongList");
                            List<SysCategory>  resultCode= new ArrayList<SysCategory>();
                            fieldInfo.get(mm).setCommonDictCode(resultCode);
                            fieldInfo.get(mm).setLinkDowmIz("1");
                            //设置联动下一个下拉框的字段值
                            if(String.valueOf(fieldInfo.get(mm).getTableTypeCode()).equals("hanDongLinkDown")){
                                fieldInfo.get(mm).setLinkDowmFieldNext(String.valueOf(otherFieldInfo[0]));
                            }else{
                                fieldInfo.get(mm).setLinkDowmFieldNext(String.valueOf(otherFieldInfo[nn+1]));
                            }
                        }

                    }
                }
            }

            BeanUtils.copyProperties(onlCgformHead, onlCgformHeadVO);
            //log.info(fieldInfo.toString());
            onlCgformHeadVO.setFieldAll(fieldInfo);
        }
        BeanUtils.copyProperties(onlCgformHead, onlCgformHeadVO);
        return onlCgformHeadVO;
    }
    
  //整理前端数据字段显示数据信息
    public Integer  topDataInfo(String kongjianMingCheng,Integer dataInfo){
        switch (kongjianMingCheng) {
            case "hanDongCheckbox":
                return dataInfo;
            case "hanDongPca":
                return dataInfo;
            case "hanDongPopup":
                return dataInfo;
            case "hanDongRadio":
                return dataInfo;
            case "hanDongSelDepart":
                return dataInfo;
            case "hanDongSelSearch":
                return dataInfo;
            case "hanDongSelTree":
                return dataInfo;
            case "hanDongSelUser":
                return dataInfo;
            case "hanDongInput":
                return dataInfo;
            case "hanDongTextArea":
                return dataInfo;
            case "hanDongUmeditor":
                return Integer.valueOf(24);
            case "hanDongDate":
                return dataInfo;
            case "hanDongPassword":
                return dataInfo;
            case "hanDongCatTree":
                return dataInfo;
            case "hanDongTime":
                return dataInfo;
            case "hanDongSwitch":
                return dataInfo;
            case "hanDongDateTime":
                return dataInfo;
            case "hanDongFile":
                return dataInfo;
            case "hanDongImage":
                return dataInfo;
            case "hanDongLinkDown":
                return dataInfo;
            case "hanDongList":
                return dataInfo;
            case "hanDongListMulti":
                return dataInfo;
            case "hanDongMarkDown":
                return Integer.valueOf(24);
            default:
                return Integer.valueOf(24);
        }
    }
    
    //整理字段返回数据信息
    private  OnlCgformFieldVO backFieldDataInfo(OnlCgformField onlCgformField){
        OnlCgformFieldVO resultInfo = new OnlCgformFieldVO();
        List<BpmToolDesigner> bpmToolDesignerAll =  bpmToolDesignerService.list();
        BpmToolDesigner bpmToolDesignerInfo = toolDesigner(bpmToolDesignerAll,String.valueOf(onlCgformField.getFieldShowType()));
        resultInfo.setTableTypeCode(String.valueOf(bpmToolDesignerInfo.getFieldType()));
        resultInfo.setTableTypeDesc(String.valueOf(toolDesignerDesc(onlCgformField)));
        resultInfo.setRuleAll(toolRuleInfo(onlCgformField));

        BeanUtils.copyProperties(onlCgformField, resultInfo);

        //判断字段控件类型分别调用不同的数据方法进行组装数据信息
        String  dataInfo = String.valueOf(onlCgformField.getFieldShowType());

        switch (dataInfo) {
            case "radio":
                resultInfo.setCommonDictCode(backfieldDataTableOrCode(onlCgformField));
                break;
            case "checkbox":
                resultInfo.setCommonDictCodeCheckBox(backfieldDataTableOrCode(onlCgformField));
                break;
            case "list":
                resultInfo.setCommonDictCode(backfieldDataTableOrCode(onlCgformField));
                break;
            case "list_multi":
                resultInfo.setCommonDictCode(backfieldDataTableOrCode(onlCgformField));
                break;
            case "sel_search":
                resultInfo.setCommonDictCode(backfieldDataTableOrCode(onlCgformField));
                break;
            case "switch":
                SwitchDataInfo switchDataInfo = backFieldDataSwitch(onlCgformField);
                resultInfo.setActiveValue(String.valueOf(switchDataInfo.getActiveValue()));
                resultInfo.setActiveText(String.valueOf(switchDataInfo.getActiveText()));
                resultInfo.setInactiveValue(String.valueOf(switchDataInfo.getInactiveValue()));
                resultInfo.setInactiveText(String.valueOf(switchDataInfo.getInactiveText()));
                break;
            case "cat_tree":
                resultInfo.setCommonCateDictCode(backfieldDataCateDictCode(onlCgformField));
                break;
            case "sel_tree":
                resultInfo.setCommonCateDictCode(backfieldSelTreeDataCateDictCode(onlCgformField));
                break;
            case "link_down":
                LinkDownDataVO linkDownDataVO = linDownDataInfo(onlCgformField);
                resultInfo.setCommonLinkDownCode(linkDownDataVO.getCommonLinkDownCode());
                resultInfo.setLinkDowmInfomation(linkDownDataVO.getLinkDowmInfomation());
                break;
            default:
                break;
        }
        return resultInfo;
    }
    
  //link_down组件数据信息
    public LinkDownDataVO linDownDataInfo(OnlCgformField onlCgformField){
        LinkDownDataVO linkDownDataVO = new LinkDownDataVO();
        if(String.valueOf(onlCgformField.getDictTable()).length()>0){
            Map mapTypes = JSON.parseObject(String.valueOf(onlCgformField.getDictTable()));
            log.info("这个是用JSON类的parseObject来解析JSON字符串!!!");
            String dataId = "";
            String dictText = "";
            String dictTable = "";
            String dataParentId = "";
            String dictField = "";
            String condition = "";
            String linkFieldInfo = "";
            for (Object obj : mapTypes.keySet()){
                if(String.valueOf(obj).equals("idField")){
                    dataId = String.valueOf(mapTypes.get(obj));
                }
                if(String.valueOf(obj).equals("txt")){
                    dictText = String.valueOf(mapTypes.get(obj));
                }
                if(String.valueOf(obj).equals("table")){
                    dictTable = String.valueOf(mapTypes.get(obj));
                }
                if(String.valueOf(obj).equals("pidField")){
                    dataParentId = String.valueOf(mapTypes.get(obj));
                }
                if(String.valueOf(obj).equals("condition")){
                    condition = String.valueOf(mapTypes.get(obj));
                    String[]   eqData =  condition.split("=");
                    String dictFieldStart = eqData[1].replace("'","");
                    String dictFieldEnd = dictFieldStart.replace("'","");
                    dictField = dictFieldEnd.replace(" ","");
                }
                if(String.valueOf(obj).equals("linkField")){
                    linkFieldInfo = String.valueOf(mapTypes.get(obj));
                }
                log.info("key为："+obj+"值为："+mapTypes.get(obj));
            }
            List<LinkDownCateCode> sysCateDictCodeLinkDown = backfieldSelTreeDataCateDictCodeLinkDown(dataId,dataParentId,dictField,dictText,dictTable);
            linkDownDataVO.setCommonLinkDownCode(sysCateDictCodeLinkDown);

            linkDownDataVO.setLinkDowmInfomation(String.valueOf(linkFieldInfo));
        }
        return  linkDownDataVO;
    }

    //--开关类型数据展示信息
    public SwitchDataInfo backFieldDataSwitch(OnlCgformField onlCgformField){
        SwitchDataInfo  resultDataSwitch= new SwitchDataInfo();
        if(String.valueOf(onlCgformField.getFieldExtendJson()).length()>0){
          String dataInfoStart = String.valueOf(onlCgformField.getFieldExtendJson()).replace("[","");
          String dataInfo = dataInfoStart.replace("]","");
          String[]  dataEnd = dataInfo.split(",");
          resultDataSwitch.setActiveValue(String.valueOf(dataEnd[0]));
          resultDataSwitch.setActiveText(String.valueOf("是"));
          resultDataSwitch.setInactiveValue(String.valueOf(dataEnd[1]));
          resultDataSwitch.setInactiveText(String.valueOf("否"));
        }else{
            resultDataSwitch.setActiveValue(String.valueOf("Y"));
            resultDataSwitch.setActiveText(String.valueOf("是"));
            resultDataSwitch.setInactiveValue(String.valueOf("N"));
            resultDataSwitch.setInactiveText(String.valueOf("否"));
        }

        return  resultDataSwitch;
    }

    //--字典数据展示信息
    public  List<SysCategory> backfieldDataTableOrCode(OnlCgformField onlCgformField){
        List<SysCategory>  resultCode= new ArrayList<SysCategory>();
        if(String.valueOf(onlCgformField.getDictTable()).length()>0){
            //当前走的是外表数据信息
            List<SysCategory> mapAll = hanDongYZCommonService.customDictData(String.valueOf(onlCgformField.getDictField()),String.valueOf(onlCgformField.getDictText()),String.valueOf(onlCgformField.getDictTable()));
            if(mapAll.size()>0){
                resultCode.addAll(mapAll);
            }
        }else{
            //走的是字典
            List<SysDictItem>  dataDictAll = hanDongYZCommonService.allSysDictItemInfo(String.valueOf(onlCgformField.getDictField()));
            if(dataDictAll.size()>0){
                for(int a=0;a<dataDictAll.size();a++){
                    SysCategory datamm = new SysCategory();
                    datamm.setId(String.valueOf(dataDictAll.get(a).getItemValue()));
                    datamm.setName(String.valueOf(dataDictAll.get(a).getItemText()));
                    resultCode.add(datamm);
                }
            }
        }

        return  resultCode;
    }

    //cat_tree类型数据字典控制信息
    public List<SysCateDictCode> backfieldDataCateDictCode(OnlCgformField onlCgformField){
        List<SysCateDictCode>  resultCode= new ArrayList<SysCateDictCode>();
        List<SysCateDictCode> allCateDictCode = new ArrayList<SysCateDictCode>();

        if(String.valueOf(onlCgformField.getDictField()).length()>0){
            allCateDictCode = hanDongYZCommonService.backfieldDataCateDictCodeTwo(String.valueOf(onlCgformField.getDictField()));
        }else{
            allCateDictCode = hanDongYZCommonService.backfieldDataCateDictCode(String.valueOf(0));
        }

        if(allCateDictCode.size()>0){
            for(int a=0;a<allCateDictCode.size();a++){
                List<SysCateDictCode>  childDictCode = diguiData(String.valueOf(allCateDictCode.get(a).getId()));
                allCateDictCode.get(a).setChildren(childDictCode);
                resultCode.add(allCateDictCode.get(a));
            }
        }
        return resultCode;
    }
    public List<SysCateDictCode> diguiData(String parentId){
        List<SysCateDictCode>  resultDictCode = new ArrayList<SysCateDictCode>();
        resultDictCode =  hanDongYZCommonService.backfieldDataCateDictCode(String.valueOf(parentId));
        if(resultDictCode.size()>0){
            for(int c=0;c<resultDictCode.size();c++){
                List<SysCateDictCode>  dataDictCode =  hanDongYZCommonService.backfieldDataCateDictCode(String.valueOf(resultDictCode.get(c).getId()));
                if(dataDictCode.size()>0){
                    List<SysCateDictCode>  dataDictCodeNew = diguiData(String.valueOf(resultDictCode.get(c).getId()));
                    resultDictCode.get(c).setChildren(dataDictCodeNew);
                }else{
                    resultDictCode.get(c).setChildren(dataDictCode);
                }
            }
        }

        return resultDictCode;
    }

    //sel_tree类型数据字典控制信息
    public List<SysCateDictCode> backfieldSelTreeDataCateDictCode(OnlCgformField onlCgformField){
        List<SysCateDictCode>  resultCode= new ArrayList<SysCateDictCode>();
        String[] dataInfo =  String.valueOf(onlCgformField.getDictText()).split(",");
        List<SysCateDictCode> allCateDictCode = hanDongYZCommonService.backfieldSelTreeDataCateDictCode(String.valueOf(dataInfo[0]),String.valueOf(dataInfo[1]),String.valueOf(onlCgformField.getDictField()),String.valueOf(dataInfo[2]),String.valueOf(onlCgformField.getDictTable()));

        if(allCateDictCode.size()>0){
            for(int a=0;a<allCateDictCode.size();a++){
                List<SysCateDictCode>  childDictCode = diguiSelData(String.valueOf(dataInfo[0]),String.valueOf(dataInfo[1]),String.valueOf(allCateDictCode.get(a).getId()),String.valueOf(dataInfo[2]),String.valueOf(onlCgformField.getDictTable()));
                allCateDictCode.get(a).setChildren(childDictCode);
                resultCode.add(allCateDictCode.get(a));
            }
        }
        return resultCode;
    }

    public List<SysCateDictCode> diguiSelData(String dataId,String dataParentId,String dictField, String dictText, String dictTable){
        List<SysCateDictCode>  resultDictCode = new ArrayList<SysCateDictCode>();
        resultDictCode =  hanDongYZCommonService.backfieldSelTreeDataCateDictCode(dataId,dataParentId,dictField,dictText,dictTable);
        if(resultDictCode.size()>0){
            for(int c=0;c<resultDictCode.size();c++){
                List<SysCateDictCode>  dataDictCode =  hanDongYZCommonService.backfieldSelTreeDataCateDictCode(dataId,dataParentId,String.valueOf(resultDictCode.get(c).getId()),dictText,dictTable);
                if(dataDictCode.size()>0){
                    List<SysCateDictCode>  dataDictCodeNew = diguiSelData(dataId,dataParentId,String.valueOf(resultDictCode.get(c).getId()),dictText,dictTable);
                    resultDictCode.get(c).setChildren(dataDictCodeNew);
                }else{
                    resultDictCode.get(c).setChildren(dataDictCode);
                }
            }
        }

        return resultDictCode;
    }


    public List<LinkDownCateCode> backfieldSelTreeDataCateDictCodeLinkDown(String dataId,String dataParentId,String dictField, String dictText, String dictTable){
        List<LinkDownCateCode>  resultCodeLinkDown= new ArrayList<LinkDownCateCode>();
        List<LinkDownCateCode> allCateDictCodeLinkDown = hanDongYZCommonService.backfieldSelTreeDataCateDictCodeLinkDown(dataId,dataParentId,dictField,dictText,dictTable);
        if(allCateDictCodeLinkDown.size()>0){
            for(int a=0;a<allCateDictCodeLinkDown.size();a++){
                List<LinkDownCateCode>  childDictCode = diguiLinkDownData(dataId,dataParentId,String.valueOf(allCateDictCodeLinkDown.get(a).getId()),dictText,dictTable);
                allCateDictCodeLinkDown.get(a).setChildren(childDictCode);
                resultCodeLinkDown.add(allCateDictCodeLinkDown.get(a));
            }
        }
        return resultCodeLinkDown;
    }


    public List<LinkDownCateCode> diguiLinkDownData(String dataId,String dataParentId,String dictField, String dictText, String dictTable){
        List<LinkDownCateCode>  resultDictCodeLinkDown = new ArrayList<LinkDownCateCode>();
        resultDictCodeLinkDown =  hanDongYZCommonService.backfieldSelTreeDataCateDictCodeLinkDown(dataId,dataParentId,dictField,dictText,dictTable);
        if(resultDictCodeLinkDown.size()>0){
            for(int c=0;c<resultDictCodeLinkDown.size();c++){
                List<LinkDownCateCode>  dataDictCode =  hanDongYZCommonService.backfieldSelTreeDataCateDictCodeLinkDown(dataId,dataParentId,String.valueOf(resultDictCodeLinkDown.get(c).getId()),dictText,dictTable);
                if(dataDictCode.size()>0){
                    List<LinkDownCateCode>  dataDictCodeNew = diguiLinkDownData(dataId,dataParentId,String.valueOf(resultDictCodeLinkDown.get(c).getId()),dictText,dictTable);
                    resultDictCodeLinkDown.get(c).setChildren(dataDictCodeNew);
                }else{
                    resultDictCodeLinkDown.get(c).setChildren(dataDictCode);
                }
            }
        }

        return resultDictCodeLinkDown;
    }

    //根据控件类型查询对应的前端控件编码
    public BpmToolDesigner toolDesigner(List<BpmToolDesigner> bpmToolDesignerAll,String fieldType){
        BpmToolDesigner bpmToolDesigner = new BpmToolDesigner();
        for(int qq=0;qq<bpmToolDesignerAll.size();qq++){
            if(String.valueOf(bpmToolDesignerAll.get(qq).getFieldShowType()).equals(fieldType)){
                bpmToolDesigner = bpmToolDesignerAll.get(qq);
            }
        }
        return bpmToolDesigner;
    }

    //根据控件类型返回前端提示显示信息
    public String toolDesignerDesc(OnlCgformField  onlCgformField){
        String  dataInfo = String.valueOf(onlCgformField.getFieldShowType());

        switch (dataInfo) {
            case "text":
                if(String.valueOf(onlCgformField.getDbDefaultVal()).length()>0){
                      return  String.valueOf(onlCgformField.getDbDefaultVal());
                }else{
                    return "请输入"+String.valueOf(onlCgformField.getDbFieldTxt());
                }

            case "radio":
                return "请选择"+String.valueOf(onlCgformField.getDbFieldTxt());
            case "checkbox":
                return "请选择"+String.valueOf(onlCgformField.getDbFieldTxt());
            case "list":
                return "请选择"+String.valueOf(onlCgformField.getDbFieldTxt());
            case "popup":
                return "单击"+String.valueOf(onlCgformField.getDbFieldTxt());
            case "cat_tree":
                return "请选择"+String.valueOf(onlCgformField.getDbFieldTxt());
            case "pca":
                return "请选择"+String.valueOf(onlCgformField.getDbFieldTxt());
            case "sel_tree":
                return "请选择"+String.valueOf(onlCgformField.getDbFieldTxt());
            default:
                return String.valueOf(onlCgformField.getDbFieldTxt());
        }
    }

    //根据控件校验规则返回校验提交是信息
    public List<OnlCgformFieldRuleVO> toolRuleInfo(OnlCgformField  onlCgformField){
        List<OnlCgformFieldRuleVO> resultFieldRule =  new  ArrayList<OnlCgformFieldRuleVO>();
        if(String.valueOf(onlCgformField.getDbIsNull()).equals("0")){
            OnlCgformFieldRuleVO onlCgformFieldRuleVO =new OnlCgformFieldRuleVO();
            onlCgformFieldRuleVO.setRequired(true);
            onlCgformFieldRuleVO.setTrigger("change");
            onlCgformFieldRuleVO.setValidator("handongRule.handongNotNull");
            resultFieldRule.add(onlCgformFieldRuleVO);
        }
        if(String.valueOf(onlCgformField.getFieldValidType()).equals("only")){

        }else if(String.valueOf(onlCgformField.getFieldValidType()).length()>0){
            QueryWrapper<BpmToolRule>  toolInfo = new QueryWrapper<BpmToolRule>();
            toolInfo.eq("field_vaild_type",String.valueOf(onlCgformField.getFieldValidType()));
            List<BpmToolRule>  bpmToolRules = bpmToolRuleService.list(toolInfo);
            OnlCgformFieldRuleVO onlCgformFieldRuleVOSecond =new OnlCgformFieldRuleVO();
            if(bpmToolRules.size()>0){
                BpmToolRule  bpmToolRule = bpmToolRules.get(0);
                onlCgformFieldRuleVOSecond.setValidator(String.valueOf(bpmToolRule.getRuleType()));
            }
            onlCgformFieldRuleVOSecond.setRequired(true);
            onlCgformFieldRuleVOSecond.setTrigger("change");
            resultFieldRule.add(onlCgformFieldRuleVOSecond);
        }
        return resultFieldRule;
    }

    //录入的online表单数据保存
	@Override
	public void save(OnlCgformDataVo onlCgformDataVo)
			throws BusinessException {
		OnlCgformHead onlCgFormHead = onlCgformHeadService.getById(onlCgformDataVo.getFormId());
		onlCgformSqlService.saveBatchOnlineTable(onlCgFormHead, onlCgformDataVo.getFieldList(), onlCgformDataVo.getDataList());		
		//onlCgformSqlService.saveBatchOnlineTable(onlCgformDataVo.getHead(), onlCgformDataVo.getFieldList(), onlCgformDataVo.getDataList());
		
	}

}
