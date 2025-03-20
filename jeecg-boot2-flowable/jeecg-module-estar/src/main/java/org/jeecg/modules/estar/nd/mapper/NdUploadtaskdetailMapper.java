package org.jeecg.modules.estar.nd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.estar.nd.entity.NdUploadtaskdetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: nd_uploadtaskdetail
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
public interface NdUploadtaskdetailMapper extends BaseMapper<NdUploadtaskdetail> {
	List<Integer> selectUploadedChunkNumList(String identifier);
}
