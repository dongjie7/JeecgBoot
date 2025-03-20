package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwProjectVersionLog;
import org.jeecg.modules.estar.tw.mapper.TwProjectVersionLogMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectVersionLogService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目版本日志表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwProjectVersionLogServiceImpl extends ServiceImpl<TwProjectVersionLogMapper, TwProjectVersionLog> implements ITwProjectVersionLogService {

	@Override
	public IPage<Map> getProjectVersionBySourceId(IPage<Map> page, String sourceId) {
		return baseMapper.selectProjectVersionLogBySourceId(page, sourceId);
	}

	@Override
	public List<Map> getProjectVersionLogBySourceIdAll(String sourceId) {
		return baseMapper.selectProjectVersionLogBySourceIdAll(sourceId);
	}

}
