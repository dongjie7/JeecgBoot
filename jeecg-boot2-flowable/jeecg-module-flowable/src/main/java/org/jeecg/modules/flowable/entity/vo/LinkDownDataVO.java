package org.jeecg.modules.flowable.entity.vo;

import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: WangYuZhou
 * @create: 2022-10-03 14:14
 * @description: linkDown返回数据信息
 **/

@Data
public class LinkDownDataVO implements Serializable {

    @ApiModelProperty(value = "联动数据源")
    private List<LinkDownCateCode> commonLinkDownCode;

    @ApiModelProperty(value = "linkDown联动字段项")
    private String linkDowmInfomation;
}
