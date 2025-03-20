package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkTime;
import org.jeecg.modules.estar.tw.mapper.TwTaskWorkTimeMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskWorkTimeService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务工时表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwTaskWorkTimeServiceImpl extends ServiceImpl<TwTaskWorkTimeMapper, TwTaskWorkTime> implements ITwTaskWorkTimeService {

	@Override
	public List<Map> getTaskWorkTimeByTaskId(String taskId) {
		return baseMapper.selectTaskWorkTimeByTaskId(taskId);
	}

	@Override
	public Map getTaskWorkTimeById(String id) {
		return baseMapper.selectTaskWorkTimeById(id);
	}

	@Override
	public Integer delTaskWorkTimeById(String id) {
		return baseMapper.deleteTaskWorkTimeById(id);
	}

}
