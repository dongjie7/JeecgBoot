
package org.jeecg.modules.estar.bs.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.estar.bs.dto.DataSetParamDto;
import org.jeecg.modules.estar.bs.entity.BsDataSetParam;
import org.jeecg.modules.estar.bs.param.DataSetParam;

/**
 * @author nbacheng
 * @desc DataSetParam 数据集动态参数服务接口
 * @date 2023-03-16
 **/
public interface IDataSetParamService extends IService<BsDataSetParam> {

    /**
     * 参数替换
     *
     * @param contextData
     * @param dynSentence
     * @return
     */
    String transform(Map<String, Object> contextData, String dynSentence);

    /**
     * 参数替换
     *
     * @param dataSetParamDtoList
     * @param dynSentence
     * @return
     */
    String transform(List<DataSetParamDto> dataSetParamDtoList, String dynSentence);

    /**
     * 参数校验  js脚本
     * @param dataSetParamDto
     * @return
     */
    Object verification(DataSetParamDto dataSetParamDto);

    /**
     * 参数校验  js脚本
     *
     * @param dataSetParamDtoList
     * @return
     */
    boolean verification(List<DataSetParamDto> dataSetParamDtoList, Map<String, Object> contextData);

}
