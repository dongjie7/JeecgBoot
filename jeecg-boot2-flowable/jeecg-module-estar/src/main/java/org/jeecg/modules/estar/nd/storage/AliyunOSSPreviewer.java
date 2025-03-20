package org.jeecg.modules.estar.nd.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import org.jeecg.modules.estar.nd.file.PreviewFile;
import org.jeecg.modules.estar.nd.file.Previewer;
import org.jeecg.modules.estar.nd.file.ThumbImage;
import org.jeecg.modules.estar.nd.util.EstarUtils;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class AliyunOSSPreviewer extends Previewer {


    private AliyunConfig aliyunConfig;

    public AliyunOSSPreviewer(){

    }

    public AliyunOSSPreviewer(AliyunConfig aliyunConfig, ThumbImage thumbImage) {
        this.aliyunConfig = aliyunConfig;
        setThumbImage(thumbImage);
    }


    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                EstarUtils.getAliyunObjectNameByFileUrl(previewFile.getFileUrl()));
        InputStream inputStream = ossObject.getObjectContent();
        previewFile.setOssClient(ossClient);
        return inputStream;
    }

}
