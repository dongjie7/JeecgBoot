package org.jeecg.modules.estar.nd.service;

import org.jeecg.modules.estar.nd.dto.BatchMoveFileDTO;
import org.jeecg.modules.estar.nd.dto.CopyFileDTO;
import org.jeecg.modules.estar.nd.dto.CreateFileDTO;
import org.jeecg.modules.estar.nd.dto.CreateFoldDTO;
import org.jeecg.modules.estar.nd.dto.MoveFileDTO;
import org.jeecg.modules.estar.nd.dto.PreviewDTO;
import org.jeecg.modules.estar.nd.dto.RenameFileDTO;
import org.jeecg.modules.estar.nd.dto.UpdateFileDTO;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.vo.FileDetailVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jeecg.common.api.vo.Result;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 网盘文件表
 * @Author: nbacheng
 * @Date:   2023-04-05
 * @Version: V1.0
 */
public interface INdFileService extends IService<NdFile> {

	Result<?> create(CreateFileDTO createFileDTO);

	Result<?> createFold(CreateFoldDTO createFoldDto);

	String copyFile(CopyFileDTO copyFileDTO);

	String moveFile(MoveFileDTO moveFileDto);

	Result<?> getFileTree();

	String renameFile(RenameFileDTO renameFileDto);

	Result<?> updateFile(UpdateFileDTO updateFileDTO);
	
	Long getFilePointCount(String fileId);
	
	void updateFileDetail(String userFileId, String identifier, long fileSize);

	String batchMoveFile(BatchMoveFileDTO batchMoveFileDto);

	Result<?> createFile(@Valid CreateFileDTO createFileDTO);

	FileDetailVO getFileDetail(String userFileId);

}
