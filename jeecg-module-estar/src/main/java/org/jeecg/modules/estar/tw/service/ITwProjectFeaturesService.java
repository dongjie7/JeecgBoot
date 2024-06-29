package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectFeatures;

import java.util.Map;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 版本库表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwProjectFeaturesService extends IService<TwProjectFeatures> {

	Result<?> save(Map<String, Object> mmap);

	Result<?> edit(Map<String, Object> mmap);

	Result<?> delete(Map<String, Object> mmap);

	Result<?> listIndex(Map<String, Object> mmap);
	
	public Map getProjectFeaturesById(String id);

}
