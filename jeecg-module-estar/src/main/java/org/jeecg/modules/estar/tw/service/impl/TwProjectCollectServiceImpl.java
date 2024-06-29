package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwProjectCollect;
import org.jeecg.modules.estar.tw.mapper.TwProjectCollectMapper;
import org.jeecg.modules.estar.tw.service.ITwProjectCollectService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目收藏表
 * @Author: nbacheng
 * @Date:   2023-06-09
 * @Version: V1.0
 */
@Service
public class TwProjectCollectServiceImpl extends ServiceImpl<TwProjectCollectMapper, TwProjectCollect> implements ITwProjectCollectService {

	@Override
	public TwProjectCollect queryByProjectIdAndUserId(String projectId, String userId) {
		TwProjectCollect twProjectCollect = lambdaQuery().eq(TwProjectCollect::getProjectId,projectId).eq(TwProjectCollect::getUserId,userId).one();
		return twProjectCollect;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getProjectCollection(String id, String memberId) {
        return baseMapper.selectProjectCollection(id,memberId);
	}

}
