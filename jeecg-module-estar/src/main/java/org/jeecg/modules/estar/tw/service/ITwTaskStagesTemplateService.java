package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskStagesTemplate;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务列表模板表
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface ITwTaskStagesTemplateService extends IService<TwTaskStagesTemplate> {
   List<String> getListStages(String projectTemplateId);
}
