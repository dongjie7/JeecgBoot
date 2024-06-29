/**/
package org.jeecg.modules.estar.bs.param;

import lombok.Data;

import java.io.Serializable;
import org.jeecg.modules.estar.bs.annotation.Query;
import org.jeecg.modules.estar.bs.constant.QueryEnum;

/**
* @desc DataSet 数据集查询输入类
* @author nbacheng
* @date 2023-03-16
**/
@Data
public class DataSetParam extends PageParam implements Serializable{
    /** 数据集编码 */
    @Query(QueryEnum.LIKE)
    private String setCode;

    /** 数据集名称 */
    @Query(QueryEnum.LIKE)
    private String setName;

    /** 数据源编码 */
    @Query(QueryEnum.EQ)
    private String sourceCode;

    /** 数据集类型 */
    @Query(QueryEnum.EQ)
    private String setType;
}
