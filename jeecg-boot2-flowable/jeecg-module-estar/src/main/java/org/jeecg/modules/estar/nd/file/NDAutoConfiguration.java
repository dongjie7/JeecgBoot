package org.jeecg.modules.estar.nd.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import org.jeecg.modules.estar.nd.storage.AliyunOSSUploader;
import org.jeecg.modules.estar.nd.storage.MinioUploader;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.RedisLock;


@Slf4j
@Configuration
@EnableConfigurationProperties({NDProperties.class})
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class NDAutoConfiguration {

	@Autowired
    private NDProperties ndProperties;

    @Bean
    public NDFactory ndFactory() {
        EstarUtils.LOCAL_STORAGE_PATH = ndProperties.getLocalStoragePath();
        String bucketName = ndProperties.getBucketName();
        if (StringUtils.isNotEmpty(bucketName)) {
        	EstarUtils.ROOT_PATH = ndProperties.getBucketName();
        } else {
        	EstarUtils.ROOT_PATH = "netdisk";
        }
        return new NDFactory(ndProperties);
    }
    
    public AliyunOSSUploader aliyunOSSUploader() {
        return new AliyunOSSUploader(ndProperties.getAliyun());
    }
    @Bean
    public MinioUploader minioUploader() {
        return new MinioUploader(ndProperties.getMinio());
    }

}
