package org.jeecg.modules.estar.bs.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;
import org.jeecg.modules.estar.bs.service.IGroovyHandler;
import org.jeecg.modules.estar.bs.service.TransformStrategy;

import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: GroovyTransformServiceImpl
 * @Author: nbacheng
 * @Date:   2023-03-21
 * @Version: V1.0
 */
@Component
@Slf4j
public class GroovyTransformServiceImpl implements TransformStrategy {

    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    /**
     * 数据清洗转换 类型
     *
     * @return
     */
    @Override
    public String type() {
        return "javaBean";
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
        Class<?> clazz = groovyClassLoader.parseClass(transformScript);
        if (clazz != null) {
            try {
                Object instance = clazz.newInstance();
                if (instance!=null) {
                    if (instance instanceof IGroovyHandler) {
                        IGroovyHandler handler = (IGroovyHandler) instance;
                        return Result.OK(handler.transform(data));
                    } else {
                        System.err.println("转换失败！");
                    }
                }
            } catch (Exception e) {
                log.info("执行javaBean异常", e);
                return Result.error(ResponseCode.EXECUTE_GROOVY_ERROR, e.getMessage());
            }
        }
        return Result.OK(data);
    }
}
