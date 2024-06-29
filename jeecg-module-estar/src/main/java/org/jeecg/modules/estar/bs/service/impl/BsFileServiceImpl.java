package org.jeecg.modules.estar.bs.service.impl;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.estar.bs.constant.ResponseCode;
import org.jeecg.modules.estar.bs.entity.BsFile;
import org.jeecg.modules.estar.bs.mapper.BsFileMapper;
import org.jeecg.modules.estar.bs.service.IBsFileService;
import org.jeecg.modules.estar.bs.util.ResponseUtil;


/**
 * @Description: 大屏文件
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Service
@Slf4j
public class BsFileServiceImpl extends ServiceImpl<BsFileMapper, BsFile> implements IBsFileService {
	
	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;

	/**
	* 本地：local minio：minio 阿里：alioss
	*/
	@Value(value="${jeecg.uploadType}")
	private String uploadType;
	
	@Autowired
    private BsFileMapper bsFileMapper;
	
	@Override
    public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, String fileId) {
        try {
            // fileId必填
            if(StringUtils.isBlank(fileId)){
                Result.error(ResponseCode.FILE_ONT_EXSIT);
                return null;
            }
            // 根据fileId，从gaea_file中读出filePath
            LambdaQueryWrapper<BsFile> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(BsFile::getFileId, fileId);
            BsFile bsFile = bsFileMapper.selectOne(queryWrapper);
            if (null == bsFile) {
            	Result.error(ResponseCode.FILE_ONT_EXSIT);
            	return null;
            }

            String userAgent = request.getHeader("User-Agent");
            boolean isIEBrowser = userAgent.indexOf("MSIE") > 0;
            // 在oss中存储的文件名 402b6193e70e40a9bf5b73a78ea1e8ab.png
            String fileObjectName = bsFile.getFileId().concat(".").concat(bsFile.getFileType());
            String originalFilename = bsFile.getFileInstruction();
            if (StringUtils.isBlank(fileObjectName) || StringUtils.isBlank(originalFilename)) {
            	Result.error(ResponseCode.FILE_ONT_EXSIT);
            	return null;
            }
            if (!originalFilename.endsWith(".".concat(bsFile.getFileType()))) {
                originalFilename = originalFilename.concat(".").concat(bsFile.getFileType());
            }

            // 调用文件存储工厂，读取文件，返回字节数组
            byte[] fileBytes;
            if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
            	fileBytes = CommonUtils.downloadFileLocal(uploadpath,fileObjectName);
                
            }else{
            	fileBytes = CommonUtils.downloadFile(fileObjectName,uploadType);
            }

            // 根据文件后缀来判断，是显示图片\视频\音频，还是下载文件
            return ResponseUtil.writeBody(originalFilename, fileBytes, isIEBrowser);
        } catch (Exception e) {
            log.error("file download error", e);
            Result.error(ResponseCode.FILE_OPERATION_FAILED, e.getMessage());
            return null;
        }
    }

    /**
     * 获取文件
     *
     * @param fileId
     * @return
     * @throws Exception 
     */
    @Override
    public byte[] getFile(String fileId) throws Exception {
        // fileId必填
        if(StringUtils.isBlank(fileId)){
        	Result.error(ResponseCode.FILE_ONT_EXSIT);
        }
        // 根据fileId，从gaea_file中读出filePath
        LambdaQueryWrapper<BsFile> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(BsFile::getFileId, fileId);
        BsFile bsFile = bsFileMapper.selectOne(queryWrapper);
        if (null == bsFile) {
        	Result.error(ResponseCode.FILE_ONT_EXSIT);
        }

        // 在oss中存储的文件名 402b6193e70e40a9bf5b73a78ea1e8ab.png
        String fileObjectName = bsFile.getFileId().concat(".").concat(bsFile.getFileType());
        String originalFilename = bsFile.getFileInstruction();
        if (StringUtils.isBlank(fileObjectName) || StringUtils.isBlank(originalFilename)) {
        	Result.error(ResponseCode.FILE_ONT_EXSIT);
        }

        // 调用文件存储工厂，读取文件，返回字节数组
        if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
        	return CommonUtils.downloadFileLocal(uploadpath,fileObjectName);
            
        }else{
        	return CommonUtils.downloadFile(fileObjectName,uploadType);
        }
    }
	
}
