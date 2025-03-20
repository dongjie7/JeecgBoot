package org.jeecg.modules.estar.bs.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;
import org.jeecg.modules.estar.bs.service.TransformStrategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Description: 字典转换
 * @Author: nbacheng
 * @Date:   2023-03-21
 * @Version: V1.0
 */
@Component
@Slf4j
public class DictTransformServiceImpl implements TransformStrategy {

    /**
     * 数据清洗转换 类型
     *
     * @return
     */
    @Override
    public String type() {
        return "dict";
    }

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    @Override
    public Result<?> transform(DataSetTransformDto def, List<JSONObject> data) {
        String transformScript = def.getTransformScript();
        if (StringUtils.isBlank(transformScript)) {
            return Result.OK(data);
        }
        JSONObject jsonObject = JSONObject.parseObject(transformScript);
        Set<String> keys = jsonObject.keySet();

        data.forEach(dataDetail -> dataDetail.forEach((key, value) -> {
            if (keys.contains(key)) {
                String string = jsonObject.getJSONObject(key).getString(value.toString());
                if (StringUtils.isNotBlank(string)) {
                    dataDetail.put(key, string);
                }
            }
        }));
        return Result.OK(data);
    }
}
