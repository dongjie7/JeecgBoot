package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskWorkTime;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务工时表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface ITwTaskWorkTimeService extends IService<TwTaskWorkTime> {
	//根据taskCode获取taskworktime
    public List<Map> getTaskWorkTimeByTaskId(String taskId);
    //根据code获取taskworktime
    public Map getTaskWorkTimeById(String id);
    //根据code删除taskworktime
    public Integer delTaskWorkTimeById(String id);
}
