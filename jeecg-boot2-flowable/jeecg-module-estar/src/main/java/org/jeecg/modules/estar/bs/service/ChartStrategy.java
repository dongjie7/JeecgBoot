package org.jeecg.modules.estar.bs.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.estar.bs.dto.ChartDto;

import java.util.List;

/**
 * Created by raodeming on 2021/4/26.
 */
public interface ChartStrategy {

    /**
     * 图表类型
     * @return
     */
    String type();

    /**
     * 针对每种图表类型做单独的数据转换解析
     *
     * @param dto
     * @return
     */
    Object transform(ChartDto dto, List<JSONObject> data);
}
