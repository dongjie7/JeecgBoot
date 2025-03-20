
package org.jeecg.modules.estar.bs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;
import org.jeecg.modules.estar.bs.entity.BsDataSetTransform;
import org.jeecg.modules.estar.bs.entity.DataSetTransform;
import org.jeecg.modules.estar.bs.mapper.BsDataSetTransformMapper;
import org.jeecg.modules.estar.bs.mapper.DataSetTransformMapper;
import org.jeecg.modules.estar.bs.service.IDataSetTransformService;
import org.jeecg.modules.estar.bs.service.TransformStrategy;

import org.jeecg.common.api.vo.Result;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @desc DataSetTransform 数据集数据转换服务实现
* @author nbacheng
* @date 2023-03-21
**/
@Service
public class DataSetTransformServiceImpl extends ServiceImpl<BsDataSetTransformMapper, BsDataSetTransform> implements IDataSetTransformService, InitializingBean, ApplicationContextAware {

    private final Map<String, TransformStrategy> queryServiceImplMap = new HashMap<>();
    private ApplicationContext applicationContext;


    public TransformStrategy getTarget(String type) {
        return queryServiceImplMap.get(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Result<?> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data) {
        if (dataSetTransformDtoList == null || dataSetTransformDtoList.size() <= 0) {
            return Result.OK(data);
        }

        for (DataSetTransformDto dataSetTransformDto : dataSetTransformDtoList) {
        	Result<?> resultlistjson =  getTarget(dataSetTransformDto.getTransformType()).transform(dataSetTransformDto, data);
        	if(resultlistjson.isSuccess()) {
        		data = (List<JSONObject>)resultlistjson.getResult();
        	}
            //data = (List<JSONObject>)getTarget(dataSetTransformDto.getTransformType()).transform(dataSetTransformDto, data).getResult();
        }
        return Result.OK(data);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, TransformStrategy> beanMap = applicationContext.getBeansOfType(TransformStrategy.class);
        //遍历该接口的所有实现，将其放入map中
        for (TransformStrategy serviceImpl : beanMap.values()) {
            queryServiceImplMap.put(serviceImpl.type(), serviceImpl);
        }
		
	}
}
