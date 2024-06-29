package org.jeecg.modules.estar.nd.service;

import org.jeecg.modules.estar.nd.dto.BatchDownloadFileDTO;
import org.jeecg.modules.estar.nd.dto.DownloadFileDTO;
import org.jeecg.modules.estar.nd.dto.PreviewDTO;
import org.jeecg.modules.estar.nd.dto.UploadFileDTO;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdStorage;
import org.jeecg.modules.estar.nd.vo.UploadFileVo;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: nd_storage
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
public interface INdStorageService extends IService<NdStorage> {

	NdStorage getStorage();
	Long getTotalStorageSize(String userId);
	Long selectStorageSizeByUserId(String userId);
	void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, String userId);
	UploadFileVo uploadFileSpeed(UploadFileDTO uploadFileDto);
	boolean checkStorage(String userId, long fileSize);
	void preview(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, PreviewDTO previewDTO);
    void downloadUserFileList(HttpServletResponse httpServletResponse, String filePath, String fileName, List<String> userFileIds);
    void previewFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO);
    void previewPictureFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO);
	void deleteFile(NdFile ndFile);
	void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			DownloadFileDTO downloadFileDTO);
	void batchDownloadFile(HttpServletResponse httpServletResponse, BatchDownloadFileDTO batchDownloadFileDTO);

}
