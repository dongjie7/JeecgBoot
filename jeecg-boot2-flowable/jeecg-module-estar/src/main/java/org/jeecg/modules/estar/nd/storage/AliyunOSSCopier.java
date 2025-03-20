package org.jeecg.modules.estar.nd.storage;

import org.apache.commons.io.IOUtils;

import com.aliyun.oss.OSS;
import org.jeecg.modules.estar.nd.file.Copier;
import org.jeecg.modules.estar.nd.file.CopyFile;
import org.jeecg.modules.estar.nd.util.EstarUtils;

import java.io.InputStream;
import java.util.UUID;

public class AliyunOSSCopier extends Copier {

    private AliyunConfig aliyunConfig;

    public AliyunOSSCopier(){

    }

    public AliyunOSSCopier(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }
    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = EstarUtils.getUploadFileUrl(uuid, copyFile.getExtendName());
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            ossClient.putObject(aliyunConfig.getOss().getBucketName(), fileUrl, inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
            ossClient.shutdown();
        }
        return fileUrl;
    }

}
