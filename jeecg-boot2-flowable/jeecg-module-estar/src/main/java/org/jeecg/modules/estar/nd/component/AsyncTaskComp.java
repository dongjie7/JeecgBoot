package org.jeecg.modules.estar.nd.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.estar.bs.util.FileUtil;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.file.CopyFile;
import org.jeecg.modules.estar.nd.file.NDFactory;
import org.jeecg.modules.estar.nd.mapper.NdFileMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdRecoveryfileService;
import org.jeecg.modules.estar.nd.service.INdStorageService;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.EstarFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 功能描述：异步任务业务类（@Async也可添加在方法上）
 */
@Slf4j
@Component
@Async("asyncTaskExecutor")
public class AsyncTaskComp {
	/**
	* 本地：local minio：minio 阿里：alioss
	*/
	@Value(value="${jeecg.uploadType}")
	private String storageType;
	
    @Resource
    INdUserfileService userFileService;
    @Resource
    NDFactory ndFactory;
    @Resource
    INdRecoveryfileService recoveryFileService;
    @Resource
    INdStorageService storageService;
    @Resource
    NdUserfileMapper userFileMapper;
    @Resource
    NdFileMapper fileMapper;
    @Resource
    FileDealComp fileDealComp;

    public Long getFilePointCount(String fileId) {
        LambdaQueryWrapper<NdUserfile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdUserfile::getFileid, fileId);
        long count = userFileMapper.selectCount(lambdaQueryWrapper);
        return count;
    }

    public Future<String> deleteUserFile(String userFileId) {
    	NdUserfile userFile = userFileService.getById(userFileId);
        if (userFile.getIsdir() == 1) {
            LambdaQueryWrapper<NdUserfile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userFileLambdaQueryWrapper.eq(NdUserfile::getDeletebatchnum, userFile.getDeletebatchnum());
            List<NdUserfile> list = userFileService.list(userFileLambdaQueryWrapper);
            recoveryFileService.deleteUserFileByDeleteBatchNum(userFile.getDeletebatchnum());
            for (NdUserfile userFileItem : list) {

                Long filePointCount = getFilePointCount(userFileItem.getFileid());

                if (filePointCount != null && filePointCount == 0 && userFileItem.getIsdir() == 0) {
                	NdFile ndFile = fileMapper.selectById(userFileItem.getFileid());
                    try {
                    	storageService.deleteFile(ndFile); 
                        fileMapper.deleteById(ndFile.getId());
                    } catch (Exception e) {
                        log.error("删除本地文件失败：" + JSON.toJSONString(ndFile));
                    }
                }
            }
        } else {

            recoveryFileService.deleteUserFileByDeleteBatchNum(userFile.getDeletebatchnum());
            Long filePointCount = getFilePointCount(userFile.getFileid());

            if (filePointCount != null && filePointCount == 0 && userFile.getIsdir() == 0) {
            	NdFile ndFile = fileMapper.selectById(userFile.getFileid());
                try {
                	storageService.deleteFile(ndFile); 
                    fileMapper.deleteById(ndFile.getId());
                } catch (Exception e) {
                    log.error("删除本地文件失败：" + JSON.toJSONString(ndFile));
                }
            }
        }

        return new AsyncResult<String>("deleteUserFile");
    }

    public Future<String> checkESUserFileId(String userFileId) {
    	NdUserfile userFile = userFileMapper.selectById(userFileId);
        if (userFile == null) {
            fileDealComp.deleteESByUserFileId(userFileId);
        }
        return new AsyncResult<String>("checkUserFileId");
    }


    public Future<String> saveUnzipFile(NdUserfile userFile, NdFile ndFile, int unzipMode, String entryName, String filePath) {
        String unzipUrl = EstarUtils.getTempFile(ndFile.getFileurl()).getAbsolutePath().replace("." + userFile.getExtendname(), "");
        String totalFileUrl = unzipUrl + entryName;
        File currentFile = new File(totalFileUrl);

        String fileId = null;
        if (!currentFile.isDirectory()) {

            FileInputStream fis = null;
            String md5Str = UUID.randomUUID().toString();
            try {
                fis = new FileInputStream(currentFile);
                md5Str = DigestUtils.md5Hex(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fis);
            }

            FileInputStream fileInputStream = null;
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("identifier", md5Str);
                List<NdFile> list = fileMapper.selectByMap(param);

                if (list != null && !list.isEmpty()) { //文件已存在
                    fileId = list.get(0).getId();
                } else { //文件不存在
                    fileInputStream = new FileInputStream(currentFile);
                    CopyFile createFile = new CopyFile();
                    createFile.setExtendName(FilenameUtils.getExtension(totalFileUrl));
                    String saveFileUrl = ndFactory.getCopier().copy(fileInputStream, createFile);
                    NdFile tempFileBean = new NdFile(saveFileUrl,  currentFile.length() , storageType, md5Str, userFile.getCreateBy());
;
                    fileMapper.insert(tempFileBean);
                    fileId = tempFileBean.getId();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fileInputStream);
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentFile.delete();
            }


        }


        EstarFile estarFile = null;
        if (unzipMode == 0) {
            estarFile = new EstarFile(userFile.getFilepath(), entryName, currentFile.isDirectory());
        } else if (unzipMode == 1) {
            estarFile = new EstarFile(userFile.getFilepath() + "/" + userFile.getFilename(), entryName, currentFile.isDirectory());
        } else if (unzipMode == 2) {
            estarFile = new EstarFile(filePath, entryName, currentFile.isDirectory());
        }

        NdUserfile saveUserFile = new NdUserfile(estarFile, userFile.getCreateBy(), fileId);
        String fileName = fileDealComp.getRepeatFileName(saveUserFile, saveUserFile.getFilepath());

        if (saveUserFile.getIsdir() == 1 && !fileName.equals(saveUserFile.getFilename())) {
            //如果是目录，而且重复，什么也不做
        } else {
            saveUserFile.setFilename(fileName);
            userFileMapper.insert(saveUserFile);
        }
        fileDealComp.restoreParentFilePath(estarFile, userFile.getCreateBy());

        return new AsyncResult<String>("saveUnzipFile");
    }


}