
package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;


/**
*
* @description 数据集数据转换 dto
* @author nbacheng
* @date 2023-03-16
**/
@Data
public class DataSetTransformDto extends JeecgEntity implements Serializable {
    /** 数据集编码 */
     private String setCode;

    /** 数据转换类型，DIC_NAME=TRANSFORM_TYPE; js，javaBean，字典转换 */
     private String transformType;

    /** 数据转换script,处理逻辑 */
     private String transformScript;

    /** 排序,执行数据转换顺序 */
     private Integer orderNum;

     /** 0--禁用 1--启用 */
     private String status;

}
