package org.jeecg.modules.estar.oa.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.oa.entity.OaCalendar;
import org.jeecg.modules.estar.oa.mapper.OaCalendarMapper;
import org.jeecg.modules.estar.oa.service.IOaCalendarService;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: OA日历表
 * @Author: nbacheng
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Service
public class OaCalendarServiceImpl extends ServiceImpl<OaCalendarMapper, OaCalendar> implements IOaCalendarService {

	@Resource
    private IEstarThirdService iEstarThirdService;
	
	@Override
	public List<OaCalendar> getList() {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		QueryWrapper<OaCalendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OaCalendar::getCreateBy,userId)
                .orderByAsc(OaCalendar::getCreateTime);
        return this.list(queryWrapper);
	}

}
