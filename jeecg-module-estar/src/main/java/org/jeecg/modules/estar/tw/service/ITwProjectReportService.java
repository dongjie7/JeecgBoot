package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectReport;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目端节点表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwProjectReportService extends IService<TwProjectReport> {
	public Map getReportByDay(String projectId, Integer day);
}
