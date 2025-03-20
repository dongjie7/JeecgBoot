/**/
package org.jeecg.modules.estar.bs.param;

import lombok.Data;

import java.io.Serializable;

import org.jeecg.modules.estar.bs.annotation.Query;
import org.jeecg.modules.estar.bs.constant.QueryEnum;

/**
* @desc DataSource 数据集查询输入类
* @author nbacheng
* @date 2023-03-16 
**/
@Data
public class DataSourceParam extends PageParam implements Serializable{

    /** 数据源名称 */
    @Query(QueryEnum.LIKE)
    private String sourceName;

    /** 数据源编码 */
    @Query(QueryEnum.LIKE)
    private String sourceCode;

    /** 数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单 */
    @Query(QueryEnum.EQ)
    private String sourceType;
}
