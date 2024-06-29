package org.jeecg.modules.estar.nd.storage;

import java.io.InputStream;

import com.aliyun.oss.OSS;
import org.jeecg.modules.estar.nd.file.WriteFile;
import org.jeecg.modules.estar.nd.file.Writer;
import org.jeecg.modules.estar.nd.util.EstarUtils;

public class AliyunOSSWriter extends Writer {

    private AliyunConfig aliyunConfig;

    public AliyunOSSWriter(){

    }

    public AliyunOSSWriter(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);

        ossClient.putObject(aliyunConfig.getOss().getBucketName(), EstarUtils.getAliyunObjectNameByFileUrl(writeFile.getFileUrl()), inputStream);
        ossClient.shutdown();
    }



}
