package org.jeecg.modules.estar.nd.storage;

import lombok.Data;

@Data
public class AliyunOSS {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String objectName;
}
