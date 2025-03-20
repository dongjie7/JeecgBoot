package org.jeecg.modules.flowable.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-09-17 22:05
 * @description: 在线表单返回类-handongyuzhou
 **/
@Data
public class OnlCgformHeadVO extends OnlCgformHead implements Serializable {

    /**业务标题名称*/
    @Excel(name = "业务标题名称", width = 15)
    @ApiModelProperty(value = "业务标题名称")
    private String onlTitleName;

    @ApiModelProperty(value = "当前表单字段信息")
    private List<OnlCgformFieldVO> fieldAll;
}
