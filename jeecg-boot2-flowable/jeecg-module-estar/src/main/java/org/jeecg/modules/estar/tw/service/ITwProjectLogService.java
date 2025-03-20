package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProject;
import org.jeecg.modules.estar.tw.entity.TwProjectLog;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目日志表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwProjectLogService extends IService<TwProjectLog> {
	public TwProject run(Map param);
}
