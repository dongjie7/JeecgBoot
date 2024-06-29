package org.jeecg.modules.estar.nd.service;

import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.vo.FileListVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 网盘用户文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
public interface INdUserfileService extends IService<NdUserfile> {
	List<NdUserfile> selectUserFileByNameAndPath(String fileName, String filePath, String userId);
	List<NdUserfile> selectSameUserFile(String fileName, String filePath, String extendName, String userId);
	IPage<FileListVO> userFileList(String userId, String filePath, Integer currentPage, Integer pageCount);
	IPage<FileListVO> getFileByFileType(Integer fileTypeId, Integer currentPage, Integer pageCount, String userId);
	void deleteUserFile(String userFileId, String userId);
	void updateFilepathByUserFileId(String userFileId, String newfilePath, String userId);
	List<NdUserfile> selectFilePathTreeByUserId(String userId);
	List<NdUserfile> selectUserFileByLikeRightFilePath(@Param("filePath") String filePath, @Param("userId") String userId);
	void userFileCopy(String userId, String userFileId, String newfilePath);
	IPage<FileListVO> getFileByFileName(String fileName, String filePath, Integer currentPage, Integer pageCount);
}
