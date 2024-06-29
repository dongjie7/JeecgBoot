package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwTaskLike;
import org.jeecg.modules.estar.tw.mapper.TwTaskLikeMapper;
import org.jeecg.modules.estar.tw.mapper.TwTaskMapper;
import org.jeecg.modules.estar.tw.service.ITwTaskLikeService;

import java.util.Map;
import java.util.Date;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务点赞表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwTaskLikeServiceImpl extends ServiceImpl<TwTaskLikeMapper, TwTaskLike> implements ITwTaskLikeService {

	@Autowired
	TwTaskLikeMapper taskLikeMapper;
	@Autowired
	TwTaskMapper taskMapper;
	
	@Override
	public void like(Map taskMap, String username, Integer likeData) {
		Integer like = MapUtils.getInteger(taskMap,"like");
        String id = MapUtils.getString(taskMap,"id");
        LambdaUpdateWrapper<TwTaskLike> taskLikeUW = new LambdaUpdateWrapper<TwTaskLike>();
        if(0==likeData) {
            like = like-1;
            taskLikeUW.eq(TwTaskLike::getMemberId,username);
            taskLikeUW.eq(TwTaskLike::getTaskId,id);
            taskLikeMapper.delete(taskLikeUW);
        }
        if(1==likeData) {
            like = like+1;
            TwTaskLike taskLike = new TwTaskLike();
            taskLike.setCreateTime(new Date());
            taskLike.setMemberId(username);
            taskLike.setTaskId(id);
        }
        taskMapper.updateTaskLike(like,id);
	}

}
