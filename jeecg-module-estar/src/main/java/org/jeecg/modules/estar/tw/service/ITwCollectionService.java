package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 收藏表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
public interface ITwCollectionService extends IService<TwCollection> {

	void starTask(String id, String username, Integer star);

}
