package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskTag;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务标签表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwTaskTagService extends IService<TwTaskTag> {
	public IPage<Map> selectListByTaskTag(IPage<Map> page, String taskTagId);

	public Map getTaskTagById(String id);
}
