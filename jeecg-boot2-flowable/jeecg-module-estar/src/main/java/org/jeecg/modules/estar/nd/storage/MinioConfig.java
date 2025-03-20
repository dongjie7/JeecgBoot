package org.jeecg.modules.estar.nd.storage;

import lombok.Data;

@Data
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
