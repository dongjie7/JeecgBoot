
package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;


/**
*
* @description 数据集动态参数 dto
* @author nbacheng
* @date 2023-03-16
**/
@Data
public class DataSetParamDto extends JeecgEntity implements Serializable {
    /** 数据集编码 */
     private String setCode;

    /** 参数名 */
     private String paramName;

    /** 参数描述 */
     private String paramDesc;

    /** 参数类型，字典= */
     private String paramType;

    /** 参数示例项 */
     private String sampleItem;

    /** 0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG */
     private Integer requiredFlag;

    /** js校验字段值规则，满足校验返回 true */
     private String validationRules;

    /** 排序 */
     private Integer orderNum;

     /** 0--禁用 1--启用 */
     private String status;

}
