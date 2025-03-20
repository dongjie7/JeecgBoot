package org.jeecg.modules.flowable.entity.vo;

import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;

import java.io.Serializable;
import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-09-18 09:52
 * @description: 表字段组长表单数据信息-返回信息
 **/
@Data
public class OnlCgformFieldVO extends OnlCgformField implements Serializable {

    @ApiModelProperty(value = "控件类型对应的组件名称Code值")
    private String tableTypeCode;

    @ApiModelProperty(value = "前端输入框默认提示信息")
    private String tableTypeDesc;

    @ApiModelProperty(value = "前端即将要输入得值通过方法参数值来接收")
    private String commonDataInfo;


    @ApiModelProperty(value = "是开关时-数据文本")
    private String activeText;

    @ApiModelProperty(value = "是开关时-数据值")
    private String activeValue;

    @ApiModelProperty(value = "否开关时-数据文本")
    private String inactiveText;

    @ApiModelProperty(value = "否开关时-数据值")
    private String inactiveValue;

    @ApiModelProperty(value = "公共数据源--下拉款数据源-单选框数据源-多选框数据源")
    private List<SysCategory> commonDictCode;

    @ApiModelProperty(value = "多选框数据源")
    private List<SysCategory> commonDictCodeCheckBox;


    @ApiModelProperty(value = "字段校验规则数据信息")
    private List<OnlCgformFieldRuleVO> ruleAll;


    @ApiModelProperty(value = "分类字典")
    private List<SysCateDictCode> commonCateDictCode;


    @ApiModelProperty(value = "联动控件使用")
    private List<LinkDownCateCode> commonLinkDownCode;

    @ApiModelProperty(value = "linkDown联动字段项")
    private String linkDowmInfomation;

    @ApiModelProperty(value = "linkDown联动下一个字段")
    private String linkDowmFieldNext;

    @ApiModelProperty(value = "联动控件使用-当前字段选中后的子集项")
    private List<LinkDownCateCode> commonLinkDownCodeChild;

    @ApiModelProperty(value = "是否是LinkDown字段  1 是    2   否")
    private String linkDowmIz;

    @ApiModelProperty(value = "字段显示列数据信息")
    private Integer fieldDataTopInfo;
}
