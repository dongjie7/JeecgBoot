package org.jeecg.modules.estar.tw.service.impl;

import org.jeecg.modules.estar.tw.entity.TwCollection;
import org.jeecg.modules.estar.tw.mapper.TwCollectionMapper;
import org.jeecg.modules.estar.tw.service.ITwCollectionService;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 收藏表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Service
public class TwCollectionServiceImpl extends ServiceImpl<TwCollectionMapper, TwCollection> implements ITwCollectionService {

	@Override
	public void starTask(String id, String username, Integer star) {
		Map collectionMap = baseMapper.selectCollection(id,username);
        if(star>0 && MapUtils.isEmpty(collectionMap)){
        	TwCollection collection = new TwCollection();
        	collection.setSourceId(id);
        	collection.setCollType("task");
        	collection.setMemberId(username);
            save(collection);
            return ;
        }
        if(star==0){
            LambdaUpdateWrapper<TwCollection> collUQ = new LambdaUpdateWrapper<TwCollection>();
            collUQ.eq(TwCollection::getSourceId,id);
            collUQ.eq(TwCollection::getCollType,"task");
            collUQ.eq(TwCollection::getMemberId,username);
            baseMapper.delete(collUQ);
        }
		
	}

}
