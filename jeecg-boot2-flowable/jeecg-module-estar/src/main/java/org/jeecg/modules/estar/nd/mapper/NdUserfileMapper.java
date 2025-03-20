package org.jeecg.modules.estar.nd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.vo.FileListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 网盘用户文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
public interface NdUserfileMapper extends BaseMapper<NdUserfile> {
	IPage<FileListVO> selectPageVo(Page<?> page, @Param("userFile") NdUserfile userFile, @Param("fileTypeId") Integer fileTypeId);

	Long selectStorageSizeByUserId(String userId);
	
	List<NdUserfile> selectUserFileByLikeRightFilePath(@Param("filePath") String filePath, @Param("userId") String userId);

	IPage<FileListVO> selectPageVoByName(Page<FileListVO> page, NdUserfile userFile, String fileName);
}
