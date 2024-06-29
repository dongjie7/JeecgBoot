
package org.jeecg.modules.estar.bs.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeecg.common.system.base.entity.JeecgEntity;


/**
*
* @description 数据集 dto
* @author nbacheng
* @date 2023-03-16
**/
@Data
public class DataSetDto extends JeecgEntity implements Serializable {
    /** 数据集编码 */
     private String setCode;

    /** 数据集名称 */
     private String setName;

    /** 数据集描述 */
     private String setDesc;

    /** 数据集类型 */
    private String setType;


    /** 数据源编码 */
     private String sourceCode;

    /** 动态查询sql或者接口中的请求体 */
     private String dynSentence;

    /** 结果案例 */
     private String caseResult;

     /** 0--禁用 1--启用 */
     private String status;

    /** 请求参数集合 */
    private List<DataSetParamDto> dataSetParamDtoList;

    /** 数据转换集合 */
    private List<DataSetTransformDto> dataSetTransformDtoList;

    /** 传入的自定义参数*/
    private Map<String, Object> contextData;

    private Set<String> setParamList;

    /**指定字段*/
    private String fieldLabel;

}
