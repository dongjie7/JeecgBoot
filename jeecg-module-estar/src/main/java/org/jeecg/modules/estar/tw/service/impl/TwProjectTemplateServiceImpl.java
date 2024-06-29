package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwProjectTemplate;
import org.jeecg.modules.estar.tw.entity.TwTaskStagesTemplate;
import org.jeecg.modules.estar.tw.mapper.TwProjectTemplateMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectTemplateService;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesTemplateService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目模板表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */
@Service
public class TwProjectTemplateServiceImpl extends ServiceImpl<TwProjectTemplateMapper, TwProjectTemplate> implements ITwProjectTemplateService {

	@Autowired
	ITwTaskStagesTemplateService taskStagesTemplateService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveTemplate(TwProjectTemplate twProjectTemplate) {
		//保存模板
		save(twProjectTemplate);
		//保存模板任务
		TwTaskStagesTemplate tst1 = new TwTaskStagesTemplate();
		TwTaskStagesTemplate tst2 = new TwTaskStagesTemplate();
		TwTaskStagesTemplate tst3 = new TwTaskStagesTemplate();
		List<TwTaskStagesTemplate> list = new ArrayList<TwTaskStagesTemplate>();
		tst1.setName("待处理");
		tst1.setProjectTemplateId(twProjectTemplate.getId());
		tst1.setSort(0);
		list.add(tst1);
		tst2.setName("进行中");
		tst2.setProjectTemplateId(twProjectTemplate.getId());
		tst2.setSort(0);
		list.add(tst2);
		tst3.setName("已完成");
		tst3.setProjectTemplateId(twProjectTemplate.getId());
		tst3.setSort(0);
		list.add(tst3);
		taskStagesTemplateService.saveBatch(list);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeProjectTemplateAndTaskStagesTemplage(String id) {
		removeById(id);
		List<String> stageList = taskStagesTemplateService.getListStages(id);
		if (ObjectUtils.isNotEmpty(stageList)) {
			taskStagesTemplateService.removeByIds(stageList);
		}
		
	}

}
