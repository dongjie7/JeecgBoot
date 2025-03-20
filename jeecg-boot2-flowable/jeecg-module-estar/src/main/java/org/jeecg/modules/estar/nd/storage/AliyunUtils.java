package org.jeecg.modules.estar.nd.storage;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class AliyunUtils {

    public static OSS getOSSClient(AliyunConfig aliyunConfig) {
        OSS ossClient = new OSSClientBuilder().build(aliyunConfig.getOss().getEndpoint(),
                aliyunConfig.getOss().getAccessKeyId(),
                aliyunConfig.getOss().getAccessKeySecret());
        return ossClient;
    }

}
