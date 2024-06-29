package org.jeecg.modules.estar.nd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.estar.nd.entity.NdSharefile;
import org.jeecg.modules.estar.nd.vo.ShareFileListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 网盘分享文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
public interface NdSharefileMapper extends BaseMapper<NdSharefile> {

	 List<ShareFileListVO> selectShareFileList(@Param("shareBatchNum") String shareBatchNum, @Param("shareFilePath") String filePath);
}
