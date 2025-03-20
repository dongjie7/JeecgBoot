package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwTaskToTag;
import org.jeecg.modules.estar.tw.mapper.TwTaskToTagMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskToTagService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务标签映射表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwTaskToTagServiceImpl extends ServiceImpl<TwTaskToTagMapper, TwTaskToTag> implements ITwTaskToTagService {

	@Override
	public Map getTaskToTagByTagIdAndTaskId(String tagId, String taskId) {
		return baseMapper.selectTaskToTagByTagIdAndTaskId(tagId, taskId);
	}

	@Override
	public List<Map> getTaskToTagByTaskId(String taskId) {
		return baseMapper.selectTaskToTagByTaskId(taskId);
	}

}
