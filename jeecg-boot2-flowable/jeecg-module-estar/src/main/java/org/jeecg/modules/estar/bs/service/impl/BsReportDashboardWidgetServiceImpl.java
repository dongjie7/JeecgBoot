package org.jeecg.modules.estar.bs.service.impl;

import org.jeecg.modules.estar.bs.entity.BsReportDashboardWidget;
import org.jeecg.modules.estar.bs.mapper.BsReportDashboardWidgetMapper;
import org.jeecg.modules.estar.bs.service.IBsReportDashboardWidgetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 大屏组件表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Service
public class BsReportDashboardWidgetServiceImpl extends ServiceImpl<BsReportDashboardWidgetMapper, BsReportDashboardWidget> implements IBsReportDashboardWidgetService {

	@Autowired
    private BsReportDashboardWidgetMapper reportDashboardWidgetMapper;

  
    @Override
    public BsReportDashboardWidget getDetail(String id) {
    	QueryWrapper<BsReportDashboardWidget> queryWrapper = new QueryWrapper<BsReportDashboardWidget>();
    	queryWrapper.eq("id", id);
    	BsReportDashboardWidget reportDashboardWidget = this.getOne(queryWrapper);

        return reportDashboardWidget;
    }
}
