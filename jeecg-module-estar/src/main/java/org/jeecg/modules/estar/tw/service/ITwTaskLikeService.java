package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwTaskLike;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务点赞表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwTaskLikeService extends IService<TwTaskLike> {

	public void like(Map taskMap, String username, Integer likeData);

}
