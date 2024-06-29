package org.jeecg.modules.estar.oa.service;

import org.jeecg.modules.estar.oa.entity.OaCalendar;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: OA日历表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
public interface IOaCalendarService extends IService<OaCalendar> {

	List<OaCalendar> getList();


}
