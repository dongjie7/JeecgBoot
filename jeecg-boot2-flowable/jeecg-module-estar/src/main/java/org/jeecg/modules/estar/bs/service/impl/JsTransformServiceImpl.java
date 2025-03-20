package org.jeecg.modules.estar.bs.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;
import org.jeecg.modules.estar.bs.service.TransformStrategy;

import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

/**
 * @Description: JsTransformServiceImpl
 * @Author: nbacheng
 * @Date:   2023-03-21
 * @Version: V1.0
 */
@Component
@Slf4j
public class JsTransformServiceImpl implements TransformStrategy {

    private ScriptEngine engine;
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
    }

    /**
     * 数据清洗转换 类型
     *
     * @return
     */
    @Override
    public String type() {
        return "js";
    }

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    @Override
    public Result<?> transform(DataSetTransformDto def, List<JSONObject> data) {
        return getValueFromJs(def,data);
    }

    private Result<?> getValueFromJs(DataSetTransformDto def, List<JSONObject> data) {
        String js = def.getTransformScript();
        try {
            engine.eval(js);
            if(engine instanceof Invocable){
                Invocable invocable = (Invocable) engine;
                return Result.OK((List<JSONObject>) invocable.invokeFunction("dataTransform", data));
            }

        } catch (Exception ex) {
            log.info("执行js异常", ex);
            return Result.error(ResponseCode.EXECUTE_JS_ERROR, ex.getMessage());
        }
        return null;
    }
}
