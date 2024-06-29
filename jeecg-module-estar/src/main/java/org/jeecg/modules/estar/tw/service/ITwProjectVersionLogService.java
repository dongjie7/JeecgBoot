package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectVersionLog;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目版本日志表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwProjectVersionLogService extends IService<TwProjectVersionLog> {
	public IPage<Map> getProjectVersionBySourceId(IPage<Map> page,String sourceId);

    public List<Map> getProjectVersionLogBySourceIdAll(String sourceId);
}
