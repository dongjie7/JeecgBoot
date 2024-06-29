package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectVersion;

import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目版本表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwProjectVersionService extends IService<TwProjectVersion> {
	void  updateSchedule(String versionId);
	TwProjectVersion getPVById(String versionId);
	Result<?> save(Map<String, Object> mmap);
	Result<?> edit(Map<String, Object> mmap);
	Result<?> delete(Map<String, Object> mmap);
	Result<?> listIndex(Map<String, Object> mmap);
	Result<?> changeStatus(Map<String, Object> mmap);
	Result<?> getVersionTask(Map<String, Object> mmap);
	Result<?> getVersionLog(Map<String, Object> mmap);
	Result<?> getVersionInfo(Map<String, Object> mmap);
	Result<?> removeVersionTask(Map<String, Object> mmap) throws Exception;
	Result<?> addVersionTask(Map<String, Object> mmap) throws Exception;
}
