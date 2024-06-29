package org.jeecg.modules.estar.bs.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
* @desc IGroovyHandler
* @author nbacheng
* @date 2023-03-21
**/
public interface IGroovyHandler {

    List<JSONObject> transform(List<JSONObject> data);
}
