package org.jeecg.modules.estar.bs.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;

import java.util.List;

import org.jeecg.common.api.vo.Result;

/**
 * @Description: TransformStrategy
 * @Author: nbacheng
 * @Date:   2023-03-21
 * @Version: V1.0
 */
public interface TransformStrategy {
    /**
     * 数据清洗转换 类型
     * @return
     */
    String type();

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    Result<?> transform(DataSetTransformDto def, List<JSONObject> data);
}
