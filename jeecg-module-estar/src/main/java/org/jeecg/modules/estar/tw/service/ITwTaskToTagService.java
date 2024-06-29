package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskToTag;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务标签映射表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwTaskToTagService extends IService<TwTaskToTag> {
	public Map getTaskToTagByTagIdAndTaskId(String tagId,String taskId);

	public List<Map> getTaskToTagByTaskId(String taskId);
}
