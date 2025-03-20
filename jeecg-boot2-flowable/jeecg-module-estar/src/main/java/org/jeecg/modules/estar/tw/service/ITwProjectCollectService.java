package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwProjectCollect;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目收藏表
 * @Author: nbacheng
 * @Date:   2023-06-09
 * @Version: V1.0
 */
public interface ITwProjectCollectService extends IService<TwProjectCollect> {
	public  TwProjectCollect queryByProjectIdAndUserId(String projectId,String userId);

	public List<Map> getProjectCollection(String projectId, String memberId);
}
