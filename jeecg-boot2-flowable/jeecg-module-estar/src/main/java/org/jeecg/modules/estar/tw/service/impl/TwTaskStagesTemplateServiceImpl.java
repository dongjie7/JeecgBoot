package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwTaskStagesTemplate;
import org.jeecg.modules.estar.tw.mapper.TwTaskStagesTemplateMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesTemplateService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务列表模板表
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Service
public class TwTaskStagesTemplateServiceImpl extends ServiceImpl<TwTaskStagesTemplateMapper, TwTaskStagesTemplate> implements ITwTaskStagesTemplateService {

	@Autowired
	TwTaskStagesTemplateMapper taskStagesTemplateMapper;
	
	@Override
	public List<String> getListStages(String projectTemplateId) {
		QueryWrapper<TwTaskStagesTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("project_template_id",projectTemplateId);
		List<TwTaskStagesTemplate> list = taskStagesTemplateMapper.selectList(queryWrapper);
		List<String> stageList = new ArrayList<String>();
		if (ObjectUtils.isNotEmpty(list)) {
			for(TwTaskStagesTemplate stagetemp : list ) {
				stageList.add(stagetemp.getId());
			}
			return stageList;
		}
		else {
			return null;
		}
	}

}
