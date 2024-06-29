package org.jeecg.modules.estar.nd.file;

import javax.annotation.Resource;

import org.jeecg.modules.estar.nd.storage.AliyunConfig;
import org.jeecg.modules.estar.nd.storage.AliyunOSSCopier;
import org.jeecg.modules.estar.nd.storage.AliyunOSSDeleter;
import org.jeecg.modules.estar.nd.storage.AliyunOSSDownloader;
import org.jeecg.modules.estar.nd.storage.AliyunOSSPreviewer;
import org.jeecg.modules.estar.nd.storage.AliyunOSSReader;
import org.jeecg.modules.estar.nd.storage.AliyunOSSUploader;
import org.jeecg.modules.estar.nd.storage.AliyunOSSWriter;
import org.jeecg.modules.estar.nd.storage.LocalStorageCopier;
import org.jeecg.modules.estar.nd.storage.LocalStorageDeleter;
import org.jeecg.modules.estar.nd.storage.LocalStorageDownloader;
import org.jeecg.modules.estar.nd.storage.LocalStoragePreviewer;
import org.jeecg.modules.estar.nd.storage.LocalStorageReader;
import org.jeecg.modules.estar.nd.storage.LocalStorageUploader;
import org.jeecg.modules.estar.nd.storage.LocalStorageWriter;
import org.jeecg.modules.estar.nd.storage.MinioConfig;
import org.jeecg.modules.estar.nd.storage.MinioCopier;
import org.jeecg.modules.estar.nd.storage.MinioDeleter;
import org.jeecg.modules.estar.nd.storage.MinioDownloader;
import org.jeecg.modules.estar.nd.storage.MinioPreviewer;
import org.jeecg.modules.estar.nd.storage.MinioReader;
import org.jeecg.modules.estar.nd.storage.MinioUploader;
import org.jeecg.modules.estar.nd.storage.MinioWriter;

import cn.hutool.core.util.StrUtil;


public class NDFactory {
	private String storageType;
    private AliyunConfig aliyunConfig;
    private ThumbImage thumbImage;
    private MinioConfig minioConfig;
    @Resource
    private AliyunOSSUploader aliyunOSSUploader;
    @Resource
    private MinioUploader minioUploader;


    public NDFactory() {
    	
    }

    public NDFactory(NDProperties ndProperties) {
        this.storageType = ndProperties.getStorageType();
        this.aliyunConfig = ndProperties.getAliyun();
        this.thumbImage = ndProperties.getThumbImage();
        this.minioConfig = ndProperties.getMinio();
    }

    public Uploader getUploader() {
        Uploader uploader = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName(), this.storageType) ) {
            uploader = new LocalStorageUploader();
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName(), this.storageType)) {
            uploader = aliyunOSSUploader;
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName(), this.storageType)) {
            uploader = minioUploader;
        } 
        return uploader;
    }


    public Downloader getDownloader(String storageType) {
        Downloader downloader = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName(),storageType)) {
            downloader = new LocalStorageDownloader();
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName() , storageType)) {
            downloader = new AliyunOSSDownloader(aliyunConfig);
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName() ,storageType)) {
            downloader = new MinioDownloader(minioConfig);
        } 
        return downloader;
    }


    public Deleter getDeleter(String storageType) {
        Deleter deleter = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName() , storageType)) {
            deleter = new LocalStorageDeleter();
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName() , storageType)) {
            deleter = new AliyunOSSDeleter(aliyunConfig);
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName(), storageType)) {
            deleter = new MinioDeleter(minioConfig);
        } 
        return deleter;
    }

    public Reader getReader(String storageType) {
        Reader reader = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName(), storageType)) {
            reader = new LocalStorageReader();
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName() , storageType)) {
            reader = new AliyunOSSReader(aliyunConfig);
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName() ,storageType)) {
            reader = new MinioReader(minioConfig);
        }
        return reader;
    }

    public Writer getWriter(String storageType) {
        Writer writer = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName() , storageType)) {
            writer = new LocalStorageWriter();
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName() , storageType)) {
            writer = new AliyunOSSWriter(aliyunConfig);
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName() ,storageType)) {
            writer = new MinioWriter(minioConfig);
        }
        return writer;
    }

    public Previewer getPreviewer(String storageType) {
        Previewer previewer = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName() , storageType)) {
            previewer = new LocalStoragePreviewer(thumbImage);
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName() , storageType)) {
            previewer = new AliyunOSSPreviewer(aliyunConfig, thumbImage);
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName() , storageType)) {
            previewer = new MinioPreviewer(minioConfig, thumbImage);
        } 
        return previewer;
    }

    public Copier getCopier() {
        Copier copier = null;
        if (StrUtil.equals(StorageTypeEnum.LOCAL.getName() , storageType)) {
            copier = new LocalStorageCopier();
        } else if (StrUtil.equals(StorageTypeEnum.ALIYUN_OSS.getName() , storageType)) {
            copier = new AliyunOSSCopier(aliyunConfig);
        } else if (StrUtil.equals(StorageTypeEnum.MINIO.getName() , storageType)) {
            copier = new MinioCopier(minioConfig);
        } 
        return copier;
    }
}
