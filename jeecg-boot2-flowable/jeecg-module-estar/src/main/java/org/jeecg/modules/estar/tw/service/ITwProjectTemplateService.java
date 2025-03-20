package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目模板表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
public interface ITwProjectTemplateService extends IService<TwProjectTemplate> {

	void saveTemplate(TwProjectTemplate twProjectTemplate);

	void removeProjectTemplateAndTaskStagesTemplage(String id);

}
