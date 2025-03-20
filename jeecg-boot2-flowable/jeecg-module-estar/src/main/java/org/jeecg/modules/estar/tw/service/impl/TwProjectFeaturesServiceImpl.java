package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.tw.entity.TwAccount;
import org.jeecg.modules.estar.tw.entity.TwProjectFeatures;
import org.jeecg.modules.estar.tw.mapper.TwProjectFeaturesMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.service.ITwAccountService;
import org.jeecg.modules.estar.tw.service.ITwProjectFeaturesService;
import org.jeecg.modules.estar.tw.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

/**
 * @Description: 版本库表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwProjectFeaturesServiceImpl extends ServiceImpl<TwProjectFeaturesMapper, TwProjectFeatures> implements ITwProjectFeaturesService {

	@Autowired
	ITwAccountService accountService;
	@Autowired
	TwTaskMapper taskMapper;
	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	public Result<?> save(Map<String, Object> mmap) {
		SysUser loginUser = iEstarThirdService.getLoginUser();	
		String projectId = MapUtils.getString(mmap,"projectId");
        String description = MapUtils.getString(mmap,"description");
        String name = MapUtils.getString(mmap,"name");
        if(null == name || "".equals(name)){
            return Result.error("请填写版本库名称");
        }
        Map m = getProjectFeaturesOneByNameAndProjectId(name,projectId);
        if(MapUtils.isNotEmpty(m)){
            return Result.error("该版本库已名称存在");
        }
        String orgId = accountService.selectOrgIdByUserId(loginUser.getUsername());
        TwProjectFeatures pf = new TwProjectFeatures();
        pf.setProjectId(projectId);
        pf.setDescription(description);
        pf.setName(name);
        pf.setOrganizationId(orgId);
        return Result.OK(save(pf));
	}

	private Map getProjectFeaturesOneByNameAndProjectId(String name, String projectId) {
		return baseMapper.selectProjectFeaturesOneByNameAndprojectId(name, projectId);
	}

	@Override
	public Result<?> edit(Map<String, Object> mmap) {
		String featuresId = MapUtils.getString(mmap,"featuresId");
        String projectId = MapUtils.getString(mmap,"projectId");
        String description = MapUtils.getString(mmap,"description");
        String name = MapUtils.getString(mmap,"name");
        if(StringUtils.isEmpty(name)){
            return Result.error("请填写版本库名称");
        }
        if(StringUtils.isEmpty(featuresId)){
            return Result.error("请选择一个版本库");
        }
        Map m =getProjectFeaturesOneByNameAndProjectId(name,projectId);
        if(MapUtils.isNotEmpty(m)){
            return Result.error("该版本库已名称存在");
        }
        m = getProjectFeaturesById(featuresId);
        TwProjectFeatures pf = new TwProjectFeatures();
        pf.setId(MapUtils.getString(m,"id"));
        pf.setName(name);pf.setDescription(description);
        return Result.OK(updateById(pf));
	}

	@Override
	public Map getProjectFeaturesById(String featuresId) {
		return baseMapper.selectProjectFeaturesById(featuresId);
	}

	@Override
	public Result<?> delete(Map<String, Object> mmap) {
		String featuresId = MapUtils.getString(mmap,"featuresId");
        if(StringUtils.isEmpty(featuresId)){
            return Result.error("请选择一个版本库");
        }
        return Result.OK(delProjectFeaturesAndTask(featuresId));
	}

	@Transactional
    Integer delProjectFeaturesAndTask(String featuresId) {
		Integer i1= baseMapper.deleteProjectFeaturesById(featuresId);
        Integer i2 = taskMapper.updateTaskFeaAndVerByFeaId(featuresId);
        return i1+i2;
	}

	@Override
	public Result<?> listIndex(Map<String, Object> mmap) {
		String projectId = MapUtils.getString(mmap,"projectId");
        if(StringUtils.isEmpty(projectId)){
            return Result.error("请选择一个项目");
        }
        return Result.OK(getProjectFeaturesByProjectId(projectId));
	}

	private List<Map> getProjectFeaturesByProjectId(String projectId) {
		return baseMapper.selectProjectFeaturesByprojectId(projectId);
	}

}
