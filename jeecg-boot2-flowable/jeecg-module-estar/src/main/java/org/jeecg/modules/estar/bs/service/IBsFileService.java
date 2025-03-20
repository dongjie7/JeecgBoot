package org.jeecg.modules.estar.bs.service;

import org.jeecg.modules.estar.bs.entity.BsFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 大屏文件
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
public interface IBsFileService extends IService<BsFile> {

	/**
     * 根据fileId显示图片或者下载文件
     *
     * @param request
     * @param response
     * @param fileId
     * @return
     */
    ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, String fileId);

    /**
     * 获取文件
     * @param fileId
     * @return
     * @throws Exception 
     */
    byte[] getFile(String fileId) throws Exception;
}
