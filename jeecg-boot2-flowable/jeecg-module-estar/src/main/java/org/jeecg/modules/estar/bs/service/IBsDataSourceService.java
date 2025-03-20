package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.dto.DataSetDto;
import org.jeecg.modules.estar.bs.dto.DataSourceDto;
import org.jeecg.modules.estar.bs.entity.BsDataSource;
import org.jeecg.modules.estar.bs.enums.BaseOperationEnum;
import org.jeecg.modules.estar.bs.param.ConnectionParam;

import java.util.List;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bs_data_source
 * @Author: nbacheng
 * @Date:   2023-03-14
 * @Version: V1.0
 */
public interface IBsDataSourceService extends IService<BsDataSource> {

	/**
     * 测试 连接
     * @param connectionParam
     * @return
     */
    Result testConnection(ConnectionParam connectionParam);

	List<JSONObject> execute(DataSourceDto dto);

	long total(DataSourceDto sourceDto, DataSetDto dto);

	void processAfterOperation(BsDataSource entity, BaseOperationEnum operationEnum) throws JeecgBootException;
	
}
