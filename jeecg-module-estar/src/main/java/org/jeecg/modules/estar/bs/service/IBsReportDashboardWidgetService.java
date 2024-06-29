package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.entity.BsReportDashboardWidget;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大屏组件表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
public interface IBsReportDashboardWidgetService extends IService<BsReportDashboardWidget> {

	/***
     * 查询详情
     *
     * @param id
     */
	BsReportDashboardWidget getDetail(String id);
}
