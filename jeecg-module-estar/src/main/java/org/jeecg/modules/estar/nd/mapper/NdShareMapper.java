package org.jeecg.modules.estar.nd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.estar.nd.entity.NdShare;
import org.jeecg.modules.estar.nd.vo.ShareListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 网盘分享表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
public interface NdShareMapper extends BaseMapper<NdShare> {

	List<ShareListVO> selectShareList(@Param("shareFilePath") String shareFilePath,@Param("shareBatchNum") String shareBatchNum, 
			@Param("beginCount") Long beginCount, @Param("pageCount") Long pageCount, @Param("userId") String userId);

}
