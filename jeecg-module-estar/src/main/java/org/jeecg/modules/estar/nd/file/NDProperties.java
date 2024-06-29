package org.jeecg.modules.estar.nd.file;


import org.springframework.boot.context.properties.ConfigurationProperties;

import org.jeecg.modules.estar.nd.storage.AliyunConfig;
import org.jeecg.modules.estar.nd.storage.MinioConfig;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "nbcio")
public class NDProperties {
    private String bucketName;
    private String storageType;
    private String localStoragePath;
    private AliyunConfig aliyun = new AliyunConfig();
    private ThumbImage thumbImage = new ThumbImage();
    private MinioConfig minio = new MinioConfig();
}
