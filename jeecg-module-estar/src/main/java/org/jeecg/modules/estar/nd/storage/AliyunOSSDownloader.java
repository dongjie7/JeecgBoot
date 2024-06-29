package org.jeecg.modules.estar.nd.storage;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.jeecg.modules.estar.nd.file.DownloadFile;
import org.jeecg.modules.estar.nd.file.Downloader;
import org.jeecg.modules.estar.nd.util.EstarUtils;

@Slf4j
public class AliyunOSSDownloader extends Downloader {

    private AliyunConfig aliyunConfig;

    public AliyunOSSDownloader(){

    }

    public AliyunOSSDownloader(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {

        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject;
        if (downloadFile.getRange() != null) {
            GetObjectRequest getObjectRequest = new GetObjectRequest(aliyunConfig.getOss().getBucketName(),
                    EstarUtils.getAliyunObjectNameByFileUrl(downloadFile.getFileUrl()));
            getObjectRequest.setRange(downloadFile.getRange().getStart(),
                    downloadFile.getRange().getStart() + downloadFile.getRange().getLength() - 1);
            ossObject = ossClient.getObject(getObjectRequest);
        } else {
            ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
            		EstarUtils.getAliyunObjectNameByFileUrl(downloadFile.getFileUrl()));
        }

        InputStream inputStream = ossObject.getObjectContent();

        downloadFile.setOssClient(ossClient);
        return inputStream;
    }


}
