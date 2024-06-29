package org.jeecg.modules.estar.nd.service.impl;

import org.jeecg.modules.estar.apithird.service.IEstarThirdService;
import org.jeecg.modules.estar.nd.component.FileDealComp;
import org.jeecg.modules.estar.nd.dto.BatchDownloadFileDTO;
import org.jeecg.modules.estar.nd.dto.DownloadFileDTO;
import org.jeecg.modules.estar.nd.dto.PreviewDTO;
import org.jeecg.modules.estar.nd.dto.UploadFileDTO;
import org.jeecg.modules.estar.nd.entity.NdFile;
import org.jeecg.modules.estar.nd.entity.NdImage;
import org.jeecg.modules.estar.nd.entity.NdPicturefile;
import org.jeecg.modules.estar.nd.entity.NdStorage;
import org.jeecg.modules.estar.nd.entity.NdSysparam;
import org.jeecg.modules.estar.nd.entity.NdUploadtask;
import org.jeecg.modules.estar.nd.entity.NdUploadtaskdetail;
import org.jeecg.modules.estar.nd.entity.NdUserfile;
import org.jeecg.modules.estar.nd.exception.DownloadException;
import org.jeecg.modules.estar.nd.file.DeleteFile;
import org.jeecg.modules.estar.nd.file.Deleter;
import org.jeecg.modules.estar.nd.file.DownloadFile;
import org.jeecg.modules.estar.nd.file.Downloader;
import org.jeecg.modules.estar.nd.file.PreviewFile;
import org.jeecg.modules.estar.nd.file.Previewer;
import org.jeecg.modules.estar.nd.file.Range;
import org.jeecg.modules.estar.nd.file.StorageTypeEnum;
import org.jeecg.modules.estar.nd.file.NDFactory;
import org.jeecg.modules.estar.nd.file.UploadFile;
import org.jeecg.modules.estar.nd.file.UploadFileResult;
import org.jeecg.modules.estar.nd.file.UploadFileStatusEnum;
import org.jeecg.modules.estar.nd.file.Uploader;
import org.jeecg.modules.estar.nd.mapper.NdFileMapper;
import org.jeecg.modules.estar.nd.mapper.NdImageMapper;
import org.jeecg.modules.estar.nd.mapper.NdStorageMapper;
import org.jeecg.modules.estar.nd.mapper.NdSysparamMapper;
import org.jeecg.modules.estar.nd.mapper.NdUploadtaskMapper;
import org.jeecg.modules.estar.nd.mapper.NdUploadtaskdetailMapper;
import org.jeecg.modules.estar.nd.mapper.NdUserfileMapper;
import org.jeecg.modules.estar.nd.service.INdFileService;
import org.jeecg.modules.estar.nd.service.INdPicturefileService;
import org.jeecg.modules.estar.nd.service.INdStorageService;
import org.jeecg.modules.estar.nd.service.INdUserfileService;
import org.jeecg.modules.estar.nd.util.EstarFile;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.MimeUtils;
import org.jeecg.modules.estar.nd.util.NdFileUtil;
import org.jeecg.modules.estar.nd.util.UploadException;
import org.jeecg.modules.estar.nd.vo.UploadFileVo;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: nd_storage
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */

@Slf4j
@Service
@Transactional(rollbackFor=Exception.class)
public class NdStorageServiceImpl extends ServiceImpl<NdStorageMapper, NdStorage> implements INdStorageService {

	@Resource
	INdFileService fileService;
	@Resource
	NdFileMapper fileMapper;
	@Resource
    private IEstarThirdService iEstarThirdService;
	@Resource
    NDFactory ndFactory;
	@Resource
	INdUserfileService userFileService;
	@Resource
	NdUserfileMapper userFileMapper;
	@Resource
	NdStorageMapper ndStorageMapper;
	@Resource
	NdSysparamMapper ndSysparamMapper;
	@Resource
	NdUserfileMapper ndUserfileMapper;
	@Resource
	FileDealComp fileDealComp;
	@Resource
	NdUploadtaskdetailMapper uploadTaskDetailMapper;
    @Resource
    NdUploadtaskMapper uploadTaskMapper;
    @Resource
    INdPicturefileService  pictureFileMapper;
    @Resource
    NdImageMapper imageMapper;
	
	@Override
	public NdStorage getStorage() {
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
        Long storageSize = selectStorageSizeByUserId(userId);
        NdStorage storage = new NdStorage();
        storage.setUserid(userId);
        storage.setStoragesize(storageSize);
        Long totalStorageSize = getTotalStorageSize(userId);
        storage.setTotalstoragesize(totalStorageSize);
        return storage;
	}

	@Override
	public Long getTotalStorageSize(String userId) {
		LambdaQueryWrapper<NdStorage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdStorage::getUserid , userId);

        NdStorage ndstorage = ndStorageMapper.selectOne(lambdaQueryWrapper);
        Long totalStorageSize = null;
        if (ndstorage == null || ndstorage.getTotalstoragesize() == null) {
            LambdaQueryWrapper<NdSysparam> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(NdSysparam::getSysparamkey, "totalStorageSize");
            NdSysparam sysParam = ndSysparamMapper.selectOne(lambdaQueryWrapper1);
            totalStorageSize = Long.parseLong(sysParam.getSysparamvalue());
            ndstorage = new NdStorage();
            ndstorage.setUserid(userId);
            ndstorage.setTotalstoragesize(totalStorageSize);
            save(ndstorage);
        } else  {
            totalStorageSize = ndstorage.getTotalstoragesize();
        }

        if (totalStorageSize != null) {
            totalStorageSize = totalStorageSize * 1024 * 1024;
        }
        return totalStorageSize;
	}

	@Override
	public Long selectStorageSizeByUserId(String userId) {
		return ndUserfileMapper.selectStorageSizeByUserId(userId);
	}

	@Override
	@Transactional
	public void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, String userId) {
		UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDto.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDto.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDto.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDto.getIdentifier());
        uploadFile.setTotalSize(uploadFileDto.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDto.getCurrentChunkSize());

        Uploader uploader = ndFactory.getUploader();
        if (uploader == null) {
            log.error("上传失败，请检查storageType是否配置正确");
            throw new UploadException("上传失败");
        }
        List<UploadFileResult> uploadFileResultList;
        try {
            uploadFileResultList = uploader.upload(request, uploadFile);
        } catch (Exception e) {
            log.error("上传失败，请检查NBCIO上传连接配置是否正确");
            throw new UploadException("上传失败", e);
        }
        for (int i = 0; i < uploadFileResultList.size(); i++){
            UploadFileResult uploadFileResult = uploadFileResultList.get(i);
            String relativePath = uploadFileDto.getRelativePath();
            EstarFile estarFile = null;
            if (relativePath.contains("/")) {
                estarFile = new EstarFile(uploadFileDto.getFilePath(), relativePath, false);
            } else {
                estarFile = new EstarFile(uploadFileDto.getFilePath(), uploadFileDto.getFilename(), false);
            }

            if (UploadFileStatusEnum.SUCCESS.equals(uploadFileResult.getStatus())){
            	NdFile ndFile = new NdFile(uploadFileResult);
            	ndFile.setCreateBy(userId);
                fileMapper.insert(ndFile);


                NdUserfile userFile = new NdUserfile(estarFile, userId, ndFile.getId());



                NdUserfile param = NdFileUtil.searchQiwenFileParam(userFile);
                List<NdUserfile> userFileList = userFileMapper.selectList(new QueryWrapper<>(param));
                if (userFileList.size() > 0) {
                    String fileName = fileDealComp.getRepeatFileName(userFile, userFile.getFilepath());
                    userFile.setFilename(fileName);
                }
                userFileMapper.insert(userFile);

                if (relativePath.contains("/")) {
                    fileDealComp.restoreParentFilePath(estarFile, userId);
                }

                fileDealComp.uploadESByUserFileId(userFile.getId());


                LambdaQueryWrapper<NdUploadtaskdetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(NdUploadtaskdetail::getIdentifier, uploadFileDto.getIdentifier());
                uploadTaskDetailMapper.delete(lambdaQueryWrapper);

                LambdaUpdateWrapper<NdUploadtask> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.set(NdUploadtask::getUploadstatus, UploadFileStatusEnum.SUCCESS.getCode())
                        .eq(NdUploadtask::getIdentifier, uploadFileDto.getIdentifier());
                uploadTaskMapper.update(null, lambdaUpdateWrapper);


                try {
                    if (EstarUtils.isImageFile(uploadFileResult.getExtendName())) {
                        BufferedImage src = uploadFileResult.getBufferedImage();
                        NdImage image = new NdImage();
                        image.setImagewidth(src.getWidth());
                        image.setImageheight(src.getHeight());
                        image.setFileid(ndFile.getId());
                        imageMapper.insert(image);
                    }
                } catch (Exception e) {
                    log.error("生成图片缩略图失败！", e);
                }

                fileDealComp.parseMusicFile(uploadFileResult.getExtendName(), uploadFileResult.getStorageType().getName(), uploadFileResult.getFileUrl(), ndFile.getId());

            } else if (UploadFileStatusEnum.UNCOMPLATE.equals(uploadFileResult.getStatus())) {
            	NdUploadtaskdetail uploadTaskDetail = new NdUploadtaskdetail();
                uploadTaskDetail.setFilepath(estarFile.getParent());
                uploadTaskDetail.setFilename(estarFile.getNameNotExtend());
                uploadTaskDetail.setChunknumber(uploadFileDto.getChunkNumber());
                uploadTaskDetail.setChunksize((int)uploadFileDto.getChunkSize());
                uploadTaskDetail.setRelativepath(uploadFileDto.getRelativePath());
                uploadTaskDetail.setTotalchunks(uploadFileDto.getTotalChunks());
                uploadTaskDetail.setTotalsize((int)uploadFileDto.getTotalSize());
                uploadTaskDetail.setIdentifier(uploadFileDto.getIdentifier());
                uploadTaskDetailMapper.insert(uploadTaskDetail);

            } else if (UploadFileStatusEnum.FAIL.equals(uploadFileResult.getStatus())) {
                LambdaQueryWrapper<NdUploadtaskdetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(NdUploadtaskdetail::getIdentifier, uploadFileDto.getIdentifier());
                uploadTaskDetailMapper.delete(lambdaQueryWrapper);

                LambdaUpdateWrapper<NdUploadtask> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.set(NdUploadtask::getUploadstatus, UploadFileStatusEnum.FAIL.getCode())
                        .eq(NdUploadtask::getIdentifier, uploadFileDto.getIdentifier());
                uploadTaskMapper.update(null, lambdaUpdateWrapper);
            }
        }
		
	}

	@Override
	@Transactional
	public UploadFileVo uploadFileSpeed(UploadFileDTO uploadFileDto) {
		
		SysUser loginUser = iEstarThirdService.getLoginUser();
        String userId = loginUser.getUsername();
		
		UploadFileVo uploadFileVo = new UploadFileVo();
        Map<String, Object> param = new HashMap<>();
        param.put("identifier", uploadFileDto.getIdentifier());
        List<NdFile> list = fileMapper.selectByMap(param);

        String filePath = uploadFileDto.getFilePath();
        String relativePath = uploadFileDto.getRelativePath();
        EstarFile estarFile = null;
        if (relativePath.contains("/")) {
            estarFile = new EstarFile(filePath, relativePath, false);
        } else {
            estarFile = new EstarFile(filePath, uploadFileDto.getFilename(), false);
        }

        if (list != null && !list.isEmpty()) {
        	NdFile file = list.get(0);

            NdUserfile userFile = new NdUserfile(estarFile, userId, file.getId());
            NdUserfile param1 = NdFileUtil.searchQiwenFileParam(userFile);
            List<NdUserfile> userFileList = userFileMapper.selectList(new QueryWrapper<>(param1));
            if (userFileList.size() <= 0) {
                userFileMapper.insert(userFile);
                fileDealComp.uploadESByUserFileId(userFile.getId());
            }
            if (relativePath.contains("/")) {
                fileDealComp.restoreParentFilePath(estarFile, userId);
            }

            uploadFileVo.setSkipUpload(true);
        } else {
            uploadFileVo.setSkipUpload(false);

            List<Integer> uploaded = uploadTaskDetailMapper.selectUploadedChunkNumList(uploadFileDto.getIdentifier());
            if (uploaded != null && !uploaded.isEmpty()) {
                uploadFileVo.setUploaded(uploaded);
            } else {

                LambdaQueryWrapper<NdUploadtask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(NdUploadtask::getIdentifier, uploadFileDto.getIdentifier());
                List<NdUploadtask> rslist = uploadTaskMapper.selectList(lambdaQueryWrapper);
                if (rslist == null || rslist.isEmpty()) {
                	NdUploadtask uploadTask = new NdUploadtask();
                    uploadTask.setIdentifier(uploadFileDto.getIdentifier());
                    uploadTask.setUploadtime(new Date());
                    uploadTask.setUploadstatus(UploadFileStatusEnum.UNCOMPLATE.getCode());
                    uploadTask.setFilename(estarFile.getNameNotExtend());
                    uploadTask.setFilepath(estarFile.getParent());
                    uploadTask.setExtendname(estarFile.getExtendName());
                    uploadTask.setUserid(userId);
                    uploadTaskMapper.insert(uploadTask);
                }
            }

        }
        return uploadFileVo;
	}

	@Override
	public boolean checkStorage(String userId, long fileSize) {
		LambdaQueryWrapper<NdStorage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NdStorage::getUserid, userId);

        NdStorage ndstorage = ndStorageMapper.selectOne(lambdaQueryWrapper);
        Long totalStorageSize = null;
        if (ndstorage == null || ndstorage.getTotalstoragesize() == null) {
            LambdaQueryWrapper<NdSysparam> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(NdSysparam::getSysparamkey, "totalStorageSize");
            NdSysparam sysParam = ndSysparamMapper.selectOne(lambdaQueryWrapper1);
            totalStorageSize = Long.parseLong(sysParam.getSysparamvalue());
            ndstorage = new NdStorage();
            ndstorage.setUserid(userId);
            ndstorage.setTotalstoragesize(totalStorageSize); 
            save(ndstorage);
        } else  {
            totalStorageSize = ndstorage.getTotalstoragesize();
        }

        if (totalStorageSize != null) {
            totalStorageSize = totalStorageSize * 1024 * 1024;
        }

        Long storageSize = userFileMapper.selectStorageSizeByUserId(userId);
        if (storageSize == null ){
            storageSize = 0L;
        }
        if (storageSize + fileSize > totalStorageSize) {
            return false;
        }
        return true;

	}

	@Override
	public void preview(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			PreviewDTO previewDTO) {
		if (previewDTO.getPlatform() != null && previewDTO.getPlatform() == 2) {
            previewPictureFile(httpServletResponse, previewDTO);
            return ;
        }
        String token = "";
        if (StringUtils.isNotEmpty(previewDTO.getToken())) {
            token = previewDTO.getToken();
        } else {
            Cookie[] cookieArr = httpServletRequest.getCookies();
            if (cookieArr != null) {
                for (Cookie cookie : cookieArr) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
        }

        NdUserfile userFile = userFileService. getById(previewDTO.getUserFileId());
        boolean authResult = fileDealComp.checkAuthDownloadAndPreview(previewDTO.getShareBatchNum(),
                previewDTO.getExtractionCode(),
                token,
                previewDTO.getUserFileId(),
                previewDTO.getPlatform());

        if (!authResult) {
            log.error("没有权限预览！！！");
            return;
        }

        String fileName = userFile.getFilename() + "." + userFile.getExtendname();
        try {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpServletResponse.addHeader("Content-Disposition", "fileName=" + fileName);// 设置文件名
        String mime = MimeUtils.getMime(userFile.getExtendname());
        httpServletResponse.setHeader("Content-Type", mime);
        if (EstarUtils.isImageFile(userFile.getExtendname())) {
            httpServletResponse.setHeader("cache-control", "public");
        }

        NdFile ndFile = fileService.getById(userFile.getFileid());
        if (EstarUtils.isVideoFile(userFile.getExtendname()) || "mp3".equalsIgnoreCase(userFile.getExtendname()) || "flac".equalsIgnoreCase(userFile.getExtendname())) {
            //获取从那个字节开始读取文件
            String rangeString = httpServletRequest.getHeader("Range");
            int start = 0;
            if (StringUtils.isNotBlank(rangeString)) {
                start = Integer.parseInt(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            }

            Downloader downloader = ndFactory.getDownloader(ndFile.getStoragetype());
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.setFileUrl(ndFile.getFileurl());
            Range range = new Range();
            range.setStart(start);

            if (start + 1024 * 1024 * 1 >= ndFile.getFilesize().intValue()) {
                range.setLength(ndFile.getFilesize().intValue() - start);
            } else {
                range.setLength(1024 * 1024 * 1);
            }
            downloadFile.setRange(range);
            InputStream inputStream = downloader.getInputStream(downloadFile);

            OutputStream outputStream = null;
			try {
				outputStream = httpServletResponse.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {

                //返回码需要为206，代表只处理了部分请求，响应了部分数据

                httpServletResponse.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                // 每次请求只返回1MB的视频流

                httpServletResponse.setHeader("Accept-Ranges", "bytes");
                //设置此次相应返回的数据范围
                httpServletResponse.setHeader("Content-Range", "bytes " + start + "-" + (ndFile.getFilesize() - 1) + "/" + ndFile.getFilesize());
                try {
					IOUtils.copy(inputStream, outputStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


            } finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
                if (downloadFile.getOssClient() != null) {
                    downloadFile.getOssClient().shutdown();
                }
            }

        } else {
            previewFile(httpServletResponse, previewDTO);
        }
		
	}

	@Override
	public void downloadUserFileList(HttpServletResponse httpServletResponse, String filePath, String fileName,
			List<String> userFileIds) {
		String staticPath = EstarUtils.getStaticPath();
        String tempPath = staticPath + "temp" + File.separator;
        File tempDirFile = new File(tempPath);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
        }

        FileOutputStream f = null;
        try {
            f = new FileOutputStream(tempPath + fileName + ".zip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream zos = new ZipOutputStream(csum);
        BufferedOutputStream out = new BufferedOutputStream(zos);

        try {
            for (String userFileId : userFileIds) {
            	NdUserfile userFile1 = userFileMapper.selectById(userFileId);
                if (userFile1.isFile()) {
                	NdFile ndFile = fileMapper.selectById(userFile1.getFileid());
                    Downloader downloader = ndFactory.getDownloader(ndFile.getStoragetype());
                    if (downloader == null) {
                        log.error("下载失败，文件存储类型不支持下载，storageType:{}", ndFile.getStoragetype());
                        throw new UploadException("下载失败");
                    }
                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.setFileUrl(ndFile.getFileurl());
                    InputStream inputStream = downloader.getInputStream(downloadFile);
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    try {
                    	EstarFile estarFile = new EstarFile(StrUtil.removePrefix(userFile1.getFilepath(), filePath), userFile1.getFilename() + "." + userFile1.getExtendname(), false);
                        zos.putNextEntry(new ZipEntry(estarFile.getPath()));

                        byte[] buffer = new byte[1024];
                        int i = bis.read(buffer);
                        while (i != -1) {
                            out.write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                    } catch (IOException e) {
                        log.error("" + e);
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(bis);
                        try {
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                	EstarFile estarFile = new EstarFile(StrUtil.removePrefix(userFile1.getFilepath(), filePath), userFile1.getFilename(), true);
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(estarFile.getPath() + EstarFile.separator));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }

        } catch (Exception e) {
            log.error("压缩过程中出现异常:"+ e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String zipPath = "";
        try {
            Downloader downloader = ndFactory.getDownloader(StorageTypeEnum.LOCAL.getName());
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.setFileUrl("temp" + File.separator + fileName + ".zip");
            File tempFile = new File(EstarUtils.getStaticPath() + downloadFile.getFileUrl());
            httpServletResponse.setContentLengthLong(tempFile.length());
            downloader.download(httpServletResponse, downloadFile);
            zipPath = EstarUtils.getStaticPath() + "temp" + File.separator + fileName + ".zip";
        } catch (Exception e) {
            //org.apache.catalina.connector.ClientAbortException: java.io.IOException: Connection reset by peer
            if (e.getMessage().contains("ClientAbortException")) {
                //该异常忽略不做处理
            } else {
                log.error("下传zip文件出现异常：{}", e.getMessage());
            }

        } finally {
            File file = new File(zipPath);
            if (file.exists()) {
                file.delete();
            }
        }
	}

	@Override
	public void previewFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO) {
		NdUserfile userFile = userFileMapper.selectById(previewDTO.getUserFileId());
		NdFile ndFile = fileMapper.selectById(userFile.getFileid());
        Previewer previewer = ndFactory.getPreviewer(ndFile.getStoragetype());
        if (previewer == null) {
            log.error("预览失败，文件存储类型不支持预览，storageType:{}", ndFile.getStoragetype());
            throw new UploadException("预览失败");
        }
        PreviewFile previewFile = new PreviewFile();
        previewFile.setFileUrl(ndFile.getFileurl());
        try {
            if ("true".equals(previewDTO.getIsMin())) {
                previewer.imageThumbnailPreview(httpServletResponse, previewFile);
            } else {
                previewer.imageOriginalPreview(httpServletResponse, previewFile);
            }
        } catch (Exception e){
                if (e.getMessage().contains("ClientAbortException")) {
                //该异常忽略不做处理
            } else {
                log.error("预览文件出现异常：{}", e.getMessage());
            }

        }
		
	}

	@Override
	public void previewPictureFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO) {
		byte[] bytesUrl = Base64.getDecoder().decode(previewDTO.getUrl());
		NdPicturefile pictureFile = new NdPicturefile();
        pictureFile.setFileurl(new String(bytesUrl));
        pictureFile = pictureFileMapper.getOne(new QueryWrapper<>(pictureFile));
        Previewer previewer = ndFactory.getPreviewer(pictureFile.getStoragetype());
        if (previewer == null) {
            log.error("预览失败，文件存储类型不支持预览，storageType:{}", pictureFile.getStoragetype());
            throw new UploadException("预览失败");
        }
        PreviewFile previewFile = new PreviewFile();
        previewFile.setFileUrl(pictureFile.getFileurl());
//        previewFile.setFileSize(pictureFile.getFileSize());
        try {

            String mime= MimeUtils.getMime(pictureFile.getExtendname());
            httpServletResponse.setHeader("Content-Type", mime);

            String fileName = pictureFile.getFilename() + "." + pictureFile.getExtendname();
            try {
                fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            httpServletResponse.addHeader("Content-Disposition", "fileName=" + fileName);// 设置文件名

            previewer.imageOriginalPreview(httpServletResponse, previewFile);
        } catch (Exception e){
            //org.apache.catalina.connector.ClientAbortException: java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
            if (e.getMessage().contains("ClientAbortException")) {
                //该异常忽略不做处理
            } else {
                log.error("预览文件出现异常：{}", e.getMessage());
            }

        }
	}

	@Override
	public void deleteFile(NdFile ndFile) {
		Deleter deleter = null;

        deleter = ndFactory.getDeleter(ndFile.getStoragetype());
        DeleteFile deleteFile = new DeleteFile();
        deleteFile.setFileUrl(ndFile.getFileurl());
        deleter.delete(deleteFile);
	}

	@Override
	public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			DownloadFileDTO downloadFileDTO) {
		Cookie[] cookieArr = httpServletRequest.getCookies();
        String token = "";
        if (StringUtils.isNotEmpty(downloadFileDTO.getToken())) {
            token = downloadFileDTO.getToken();
        } else {
	        if (cookieArr != null) {
	            for (Cookie cookie : cookieArr) {
	                if ("token".equals(cookie.getName())) {
	                    token = cookie.getValue();
	                }
	            }
	        }
        }
        boolean authResult = fileDealComp.checkAuthDownloadAndPreview(downloadFileDTO.getShareBatchNum(),
                downloadFileDTO.getExtractionCode(),
                token,
                downloadFileDTO.getUserFileId(), null);
        if (!authResult) {
            log.error("没有权限下载！！！");
            return;
        }
        httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
        NdUserfile userFile = userFileMapper.selectById(downloadFileDTO.getUserFileId());
        String fileName = "";
        if (userFile.getIsdir() == 1) {
            fileName = userFile.getFilename() + ".zip";
        } else {
            fileName = userFile.getFilename() + "." + userFile.getExtendname();

        }
        try {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名

        if (userFile.isFile()) {

        	NdFile ndFile = fileMapper.selectById(userFile.getFileid());
            Downloader downloader = ndFactory.getDownloader(ndFile.getStoragetype());
            if (downloader == null) {
                log.error("下载失败，文件存储类型不支持下载，storageType:{}", ndFile.getStoragetype());
                throw new DownloadException("下载失败");
            }
            DownloadFile downloadFile = new DownloadFile();

            downloadFile.setFileUrl(ndFile.getFileurl());
            httpServletResponse.setContentLengthLong(ndFile.getFilesize());
            downloader.download(httpServletResponse, downloadFile);
        } else {

        	EstarFile estarFile = new EstarFile(userFile.getFilepath(), userFile.getFilename(), true);
            List<NdUserfile> userFileList = userFileMapper.selectUserFileByLikeRightFilePath(estarFile.getPath() , userFile.getCreateBy());
            List<String> userFileIds = userFileList.stream().map(NdUserfile::getId).collect(Collectors.toList());

            downloadUserFileList(httpServletResponse, userFile.getFilepath(), userFile.getFilename(), userFileIds);
        }
		
	}

	@Override
	public void batchDownloadFile(HttpServletResponse httpServletResponse, BatchDownloadFileDTO batchDownloadFileDTO) {
		String files = batchDownloadFileDTO.getUserFileIds();
        String[] userFileIdStrs = files.split(",");
        List<String> userFileIds = new ArrayList<>();
        for(String userFileId : userFileIdStrs) {
        	NdUserfile userFile = userFileService.getById(userFileId);
            if (userFile.getIsdir() == 0) {
                userFileIds.add(userFileId);
            } else {
            	EstarFile estarFile = new EstarFile(userFile.getFilepath(), userFile.getFilename(), true);
                List<NdUserfile> userFileList = userFileService.selectUserFileByLikeRightFilePath(estarFile.getPath(), userFile.getCreateBy());
                List<String> userFileIds1 = userFileList.stream().map(NdUserfile::getId).collect(Collectors.toList());
                userFileIds.add(userFile.getId());
                userFileIds.addAll(userFileIds1);
            }
            
        }
        NdUserfile userFile = userFileService.getById(userFileIdStrs[0]);
        httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
        Date date = new Date();
        String fileName = String.valueOf(date.getTime());
        httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName + ".zip");// 设置文件名
        downloadUserFileList(httpServletResponse, userFile.getFilepath(), fileName, userFileIds);
	}

}
