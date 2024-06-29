package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTask;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目任务表
 * @Author: nbacheng
 * @Date:   2023-07-01
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwTaskService extends IService<TwTask> {
	public TwTask getTaskById(String id);
	List<Map> getTaskListByVersionAndDelete(Map params);
	public Result<?> batchAssignTask(Map<String, Object> mmap);
	public Result<?> taskStagesSave(Map<String, Object> mmap);
	public Result<?> taskStagesEdit(Map<String, Object> mmap);
	public Result<?> taskStagesDel(Map<String, Object> mmap);
	public Result<?> recycleBatch(String stageId);
	public Result<?> taskSave(Map<String, Object> mmap);
	public Result<?> createTask(TwTask task,String pid);
	public Map getTaskMapById(String id);
	public Result<?> assignTask(Map<String, Object> mmap);
	public Result<?> getTaskMembers(Map<String, Object> mmap);
	public IPage<Map> taskIndex(IPage<Map> page, Map<String, Object> mmap);
	public Result<?> recovery(String taskId);
	public Map getTaskByIdNoDel(String id);
	public void star(Map taskMap, String username, Integer starData);
	public List<Map> taskSources(Map<String, Object> mmap);
	public List<Map> taskWorkTimeList(Map<String, Object> mmap);
	public Map readTask(Map<String, Object> mmap);
	public void taskRecycle(String taskId, String memberId);
	public void edit(TwTask task, String memberId);
	public Result<?> edit(String taskId, Map<String, Object> mmap);
	public Map taskLog(Map<String, Object> mmap);
	public Result<?> createComment(Map<String, Object> mmap);
	public Result<?> dateTotalForProject(Map<String, Object> mmap);
	public Result<?> taskToTags(Map<String, Object> mmap);
	public Result<?> taskGantt(Map<String, Object> mmap);
}
