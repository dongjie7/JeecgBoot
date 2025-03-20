package org.jeecg.modules.estar.nd.storage;

import com.aliyun.oss.OSS;
import org.jeecg.modules.estar.nd.file.DeleteFile;
import org.jeecg.modules.estar.nd.file.Deleter;
import org.jeecg.modules.estar.nd.util.EstarUtils;

public class AliyunOSSDeleter extends Deleter {
    private AliyunConfig aliyunConfig;

    public AliyunOSSDeleter(){

    }

    public AliyunOSSDeleter(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }
    @Override
    public void delete(DeleteFile deleteFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            ossClient.deleteObject(aliyunConfig.getOss().getBucketName(), EstarUtils.getAliyunObjectNameByFileUrl(deleteFile.getFileUrl()));
        } finally {
            ossClient.shutdown();
        }
        deleteCacheFile(deleteFile);
    }
}
