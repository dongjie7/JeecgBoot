package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwTaskTag;
import org.jeecg.modules.estar.tw.mapper.TwTaskTagMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskStagesService;
import org.jeecg.modules.estar.tw.service.ITwTaskTagService;
import org.jeecg.modules.estar.tw.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.tw.mapper.CommMapper;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

/**
 * @Description: 任务标签表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwTaskTagServiceImpl extends ServiceImpl<TwTaskTagMapper, TwTaskTag> implements ITwTaskTagService {

	@Autowired
	CommMapper commMapper;
	@Autowired
	ITwTaskStagesService taskStagesService;
	@Resource
	private IEstarThirdService iEstarThirdService;
	
	@Override
	public IPage<Map> selectListByTaskTag(IPage<Map> page, String taskTagId) {
		String sql = String.format("select t.* from tw_task_to_tag as tt join tw_task as t on tt.task_id = t.id where tt.tag_id = '%s' order by t.id desc",taskTagId);
        page = commMapper.customQueryItem(page,sql);
        List<Map> record = page.getRecords();
        List<Map> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(record)){
            record.stream().forEach(map -> {
                String taskId = MapUtils.getString(map,"id");
                map.put("tags",taskStagesService.getTagsAttr(taskId));
                map.put("executor",null);
                String assign_to = MapUtils.getString(map,"assign_to");
                if(StringUtils.isNotEmpty(assign_to)){
                    SysUser member = iEstarThirdService.getUserByUsername(MapUtils.getString(map,"assign_to"));
                    map.put("executor",member);
                }
                result.add(map);
            });
        }
        page.setRecords(result);
        return page;
	}

	@Override
	public Map getTaskTagById(String id) {
		return baseMapper.selectTaskTagById(id);
	}

}
