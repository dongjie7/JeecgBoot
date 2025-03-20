package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskStages;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务列表
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface ITwTaskStagesService extends IService<TwTaskStages> {

	Result<?> getStagesTasks(Map<String, Object> tasksmap);

	Result<?> taskDone(Map<String, Object> mmap);
	
	TwTaskStages getTaskStageById(String id);
	
	public List<Map> getTagsAttr(String taskId);

	Result<?> sortTask(Map<String, Object> mmap);
	
	public Map buildTaskMap(Map task, String memberId);
	
	public void taskHook(String memberId, String taskId, String type, String toMemberId, Integer isComment,
			String remark, String content, String fileCode, Object data, String tag);

	Result<?> getTaskStages(Map<String, Object> mmap);


}
