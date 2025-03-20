package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwProjectInfo;
import org.jeecg.modules.estar.tw.mapper.TwProjectInfoMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectInfoService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目自定义信息表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwProjectInfoServiceImpl extends ServiceImpl<TwProjectInfoMapper, TwProjectInfo> implements ITwProjectInfoService {

	@Override
	public List<Map> getProjectInfoByProjectId(String projectId) {
		return baseMapper.selectProjectInfoByProjectId(projectId);
	}

}
