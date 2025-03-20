package org.jeecg.modules.estar.bs.service.impl;

import org.jeecg.modules.estar.bs.constant.Enabled;
import org.jeecg.modules.estar.bs.constant.JdbcConstants;
import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.dto.DataSetDto;
import org.jeecg.modules.estar.bs.dto.DataSetParamDto;
import org.jeecg.modules.estar.bs.dto.DataSetTransformDto;
import org.jeecg.modules.estar.bs.dto.DataSourceDto;
import org.jeecg.modules.estar.bs.dto.OriginalDataDto;
import org.jeecg.modules.estar.bs.entity.BsDataSet;
import org.jeecg.modules.estar.bs.entity.BsDataSetParam;
import org.jeecg.modules.estar.bs.entity.BsDataSetTransform;
import org.jeecg.modules.estar.bs.entity.BsDataSource;
import org.jeecg.modules.estar.bs.entity.DataSetTransform;
import org.jeecg.modules.estar.bs.enums.SetTypeEnum;
import org.jeecg.modules.estar.bs.mapper.BsDataSetMapper;
import org.jeecg.modules.estar.bs.param.DataSetParam;
import org.jeecg.modules.estar.bs.service.IBsDataSetService;
import org.jeecg.modules.estar.bs.service.IBsDataSourceService;
import org.jeecg.modules.estar.bs.service.IDataSetParamService;
import org.jeecg.modules.estar.bs.service.IDataSetTransformService;
import org.jeecg.modules.estar.bs.util.EstarBeanUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bs_data_set
 * @Author: nbacheng
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Service
@Slf4j
public class BsDataSetServiceImpl extends ServiceImpl<BsDataSetMapper, BsDataSet> implements IBsDataSetService {

    @Autowired
    private IDataSetParamService dataSetParamService;

    @Autowired
    private IDataSetTransformService dataSetTransformService;

    @Autowired
    private IBsDataSourceService bsDataSourceService;
    
    @Autowired
    private BsDataSetMapper dataSetMapper;
	
	@Override
	public OriginalDataDto testTransform(DataSetDto dto) {
		String dynSentence = dto.getDynSentence();

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String sourceCode = dto.getSourceCode();
        //1.获取数据源
        BsDataSource bsDataSource;
        if (dto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http不需要数据源，兼容已有的逻辑，将http所需要的数据塞进DataSource
        	bsDataSource = new BsDataSource();
        	bsDataSource.setConfig(dynSentence);
        	bsDataSource.setType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            }else {
                dynSentence = "{}";
            }

        }else {
        	QueryWrapper<BsDataSource> queryWrapper = new QueryWrapper<BsDataSource>();
        	queryWrapper.eq("code", sourceCode);
        	bsDataSource  = bsDataSourceService.getOne(queryWrapper);
        }

        //3.参数替换
        //3.1参数校验
        boolean verification = dataSetParamService.verification(dto.getDataSetParamDtoList(), null);
        if (!verification) {
        	Result.error(ResponseCode.RULE_FIELDS_CHECK_ERROR);
        	return originalDataDto;
        }

        dynSentence = dataSetParamService.transform(dto.getDataSetParamDtoList(), dynSentence);
        //4.获取数据
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(bsDataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(setContextData(dto.getDataSetParamDtoList()));

        //获取total,判断DataSetParamDtoList中是否传入分页参数
        Map<String, Object> collect = dto.getDataSetParamDtoList().stream().collect(Collectors.toMap(DataSetParamDto::getParamName, DataSetParamDto::getSampleItem));
        if (collect.containsKey("pageNumber") && collect.containsKey("pageSize")) {
            dto.setContextData(collect);
            long total = bsDataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }

        List<JSONObject> data = bsDataSourceService.execute(dataSourceDto);
        //5.数据转换
        List<JSONObject> transform = (List<JSONObject>)dataSetTransformService.transform(dto.getDataSetTransformDtoList(), data).getResult();
        originalDataDto.setData(transform);
        return originalDataDto;
	}
	
	/**
     * dataSetParamDtoList转map
     * @param dataSetParamDtoList
     * @return
     */
    public Map<String, Object> setContextData(List<DataSetParamDto> dataSetParamDtoList){
        Map<String, Object> map = new HashMap<>();
        if (null != dataSetParamDtoList && dataSetParamDtoList.size() > 0) {
            dataSetParamDtoList.forEach(dataSetParamDto -> map.put(dataSetParamDto.getParamName(), dataSetParamDto.getSampleItem()));
        }
        return map;
    }

    /**
     * 获取数据
     *
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto getData(DataSetDto dto) {

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String setCode = dto.getSetCode();
        //1.获取数据集、参数替换、数据转换
        DataSetDto dataSetDto = detailSet(setCode);
        String dynSentence = dataSetDto.getDynSentence();
        //2.获取数据源
        BsDataSource bsdataSource;
        if (StringUtils.isNotBlank(dataSetDto.getSetType())
                && dataSetDto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http不需要数据源，兼容已有的逻辑，将http所需要的数据塞进DataSource
        	bsdataSource = new BsDataSource();
        	bsdataSource.setConfig(dynSentence);
        	bsdataSource.setType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            }else {
                dynSentence = "{}";
            }

        }else {
        	QueryWrapper<BsDataSource> queryWrapper = new QueryWrapper<BsDataSource>();
        	queryWrapper.eq("code", dataSetDto.getSourceCode());
        	bsdataSource  = bsDataSourceService.getOne(queryWrapper);
        }

        //3.参数替换
        //3.1参数校验
        log.debug("参数校验替换前：{}", dto.getContextData());
        boolean verification = dataSetParamService.verification(dataSetDto.getDataSetParamDtoList(), dto.getContextData());
        if (!verification) {
            Result.error(ResponseCode.RULE_FIELDS_CHECK_ERROR);
            return null;
        }
        dynSentence = dataSetParamService.transform(dto.getContextData(), dynSentence);
        log.debug("参数校验替换后：{}", dto.getContextData());
        //4.获取数据
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(bsdataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(dto.getContextData());
        //获取total,判断contextData中是否传入分页参数
        if (null != dto.getContextData()
                && dto.getContextData().containsKey("pageNumber")
                && dto.getContextData().containsKey("pageSize")) {
            long total = bsDataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }
        List<JSONObject> data = bsDataSourceService.execute(dataSourceDto);
        //5.数据转换
        List<JSONObject> transform = (List<JSONObject>)dataSetTransformService.transform(dataSetDto.getDataSetTransformDtoList(), data).getResult();
        originalDataDto.setData(transform);
        return originalDataDto;
    }
    /**
     * 单条详情
     *
     * @param setCode
     * @return
     */
    @Override
    public DataSetDto detailSet(String setCode) {
        DataSetDto dto = new DataSetDto();
        QueryWrapper<BsDataSet> queryWrapper = new QueryWrapper<BsDataSet>();
    	queryWrapper.eq("set_code", setCode);
    	BsDataSet result = this.getOne(queryWrapper);
        EstarBeanUtils.copyAndFormatter(result, dto);
        return getDetailSet(dto, setCode);
    }

    public DataSetDto getDetailSet(DataSetDto dto, String setCode) {
        //查询参数
        List<BsDataSetParam> dataSetParamList = dataSetParamService.list(
                new QueryWrapper<BsDataSetParam>()
                        .lambda()
                        .eq(BsDataSetParam::getSetCode, setCode)
        );
        List<DataSetParamDto> dataSetParamDtoList = new ArrayList<>();
        dataSetParamList.forEach(dataSetParam -> {
            DataSetParamDto dataSetParamDto = new DataSetParamDto();
            EstarBeanUtils.copyAndFormatter(dataSetParam, dataSetParamDto);
            dataSetParamDtoList.add(dataSetParamDto);
        });
        dto.setDataSetParamDtoList(dataSetParamDtoList);

        //数据转换

        List<BsDataSetTransform> dataSetTransformList = dataSetTransformService.list( 
        		new QueryWrapper<BsDataSetTransform>()
                    .lambda()
                    .eq(BsDataSetTransform::getSetCode, setCode)
                    .orderByAsc(BsDataSetTransform::getOrderNum));
        List<DataSetTransformDto> dataSetTransformDtoList = new ArrayList<>();
        dataSetTransformList.forEach(dataSetTransform -> {
            DataSetTransformDto dataSetTransformDto = new DataSetTransformDto();
            EstarBeanUtils.copyAndFormatter(dataSetTransform, dataSetTransformDto);
            dataSetTransformDtoList.add(dataSetTransformDto);
        });
        dto.setDataSetTransformDtoList(dataSetTransformDtoList);

        if (StringUtils.isNotBlank(dto.getCaseResult())) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(dto.getCaseResult());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                dto.setSetParamList(jsonObject.keySet());
            } catch (Exception e) {
                log.error("error",e);
            }
        }
        return dto;
    }

    /**
     * 获取所有数据集
     *
     * @return
     */
	@Override
	public List<BsDataSet> queryAllDataSet() {
		LambdaQueryWrapper<BsDataSet> wrapper = Wrappers.lambdaQuery();
        wrapper.select(BsDataSet::getSetCode, BsDataSet::getSetName, BsDataSet::getSetDesc, BsDataSet::getId)
                .eq(BsDataSet::getStatus, Enabled.YES.getValue());
        wrapper.orderByDesc(BsDataSet::getUpdateTime);
        return dataSetMapper.selectList(wrapper);
	}

	@Override
	public DataSetDto detailSet(Long id) {
		DataSetDto dto = new DataSetDto();
		BsDataSet result = this.getById(id);
        String setCode = result.getSetCode();
        EstarBeanUtils.copyAndFormatter(result, dto);
        return getDetailSet(dto, setCode);
	}

}
