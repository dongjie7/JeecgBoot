
package org.jeecg.modules.estar.bs.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;
import org.jeecg.modules.estar.bs.entity.BsDataSetTransform;

import java.util.List;

import org.jeecg.common.api.vo.Result;

/**
* @desc DataSetTransform 数据集数据转换服务接口
* @author nbacheng
* @date 2023-03-21
**/
public interface IDataSetTransformService extends IService<BsDataSetTransform> {

	Result<?> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data);

}
