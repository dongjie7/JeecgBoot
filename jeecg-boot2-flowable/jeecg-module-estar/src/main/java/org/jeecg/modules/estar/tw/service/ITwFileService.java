package org.jeecg.modules.estar.tw.service;

import org.jeecg.modules.estar.tw.entity.TwFile;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目文件表
 * @Author: nbacheng
 * @Date:   2023-07-11
 * @Version: V1.0
 */
public interface ITwFileService extends IService<TwFile> {

	Result<?> uploadFiles(HttpServletRequest request, MultipartFile multipartFile) throws Exception;

	Map getFileById(String fileId);

	Result<?> getProjectFile(Map<String, Object> mmap);

	Result<?> recovery(String fileId);

	Result<?> FileRecycle(String fileId);

}
