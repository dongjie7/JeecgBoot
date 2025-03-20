
package org.jeecg.modules.estar.bs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.DataSetParamDto;
import org.jeecg.modules.estar.bs.entity.BsDataSetParam;
import org.jeecg.modules.estar.bs.mapper.BsDataSetParamMapper;
import org.jeecg.modules.estar.bs.mapper.DataSetParamMapper;
import org.jeecg.modules.estar.bs.param.DataSetParam;
import org.jeecg.modules.estar.bs.service.IDataSetParamService;
import org.jeecg.modules.estar.bs.util.ParamsResolverHelper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.api.vo.Result;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @desc DataSetParam 数据集动态参数服务实现
* @author nbacheng
* @date 2023-03-16
**/
@Service
@Slf4j
public class DataSetParamServiceImpl extends ServiceImpl<BsDataSetParamMapper, BsDataSetParam> implements IDataSetParamService {

    private ScriptEngine engine;
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
    }

    @Autowired
    private DataSetParamMapper dataSetParamMapper;

    /**
     * 参数替换
     *
     * @param contextData
     * @param dynSentence
     * @return
     */
    @Override
    public String transform(Map<String, Object> contextData, String dynSentence) {
        if (StringUtils.isBlank(dynSentence)) {
            return dynSentence;
        }
        if (dynSentence.contains("${")) {
            dynSentence = ParamsResolverHelper.resolveParams(contextData, dynSentence);
        }
        if (dynSentence.contains("${")) {
            Result.error(ResponseCode.INCOMPLETE_PARAMETER_REPLACEMENT_VALUES, dynSentence);
        }
        return dynSentence;
    }

    /**
     * 参数替换
     *
     * @param dataSetParamDtoList
     * @param dynSentence
     * @return
     */
    @Override
    public String transform(List<DataSetParamDto> dataSetParamDtoList, String dynSentence) {
        Map<String, Object> contextData = new HashMap<>();
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() <= 0) {
            return dynSentence;
        }
        dataSetParamDtoList.forEach(dataSetParamDto -> {
            contextData.put(dataSetParamDto.getParamName(), dataSetParamDto.getSampleItem());
        });
        return transform(contextData, dynSentence);
    }

    /**
     * 参数校验  js脚本
     *
     * @param dataSetParamDto
     * @return
     */
    @Override
    public Object verification(DataSetParamDto dataSetParamDto) {

        String validationRules = dataSetParamDto.getValidationRules();
        if (StringUtils.isNotBlank(validationRules)) {
            try {
                engine.eval(validationRules);
                if(engine instanceof Invocable){
                    Invocable invocable = (Invocable) engine;
                    Object exec = invocable.invokeFunction("verification", dataSetParamDto);
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (exec instanceof Boolean) {
                        return objectMapper.convertValue(exec, Boolean.class);
                    }else {
                        return objectMapper.convertValue(exec, String.class);
                    }

                }

            } catch (Exception ex) {
                Result.error(ResponseCode.EXECUTE_JS_ERROR, ex.getMessage());
                return false;
            }

        }
        return true;
    }

    /**
     * 参数校验  js脚本
     *
     * @param dataSetParamDtoList
     * @return
     */
    @Override
    public boolean verification(List<DataSetParamDto> dataSetParamDtoList, Map<String, Object> contextData) {
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() == 0) {
            return true;
        }

        for (DataSetParamDto dataSetParamDto : dataSetParamDtoList) {
            if (null != contextData) {
                String value = contextData.getOrDefault(dataSetParamDto.getParamName(), "").toString();
                dataSetParamDto.setSampleItem(value);
            }

            Object verification = verification(dataSetParamDto);
            if (verification instanceof Boolean) {
                if (!(Boolean) verification) {
                    return false;
                }
            }else {
                //将得到的值重新赋值给contextData
                if (null != contextData) {
                    contextData.put(dataSetParamDto.getParamName(), verification);
                }
                dataSetParamDto.setSampleItem(verification.toString());
            }

        }
        return true;
    }

}
