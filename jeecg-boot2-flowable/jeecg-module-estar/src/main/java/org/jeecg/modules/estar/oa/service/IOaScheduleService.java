package org.jeecg.modules.estar.oa.service;

import org.jeecg.modules.estar.oa.entity.OaSchedule;
import org.jeecg.modules.estar.oa.vo.ScheduleTime;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: OA日程表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
public interface IOaScheduleService extends IService<OaSchedule> {

	List<OaSchedule> getList(ScheduleTime scheduleTime);
    void saveAndAddSchedule(OaSchedule oaSchedule);
    boolean updateSchedule(OaSchedule oaSchedule);
    boolean removeSchedule(String id);
}
