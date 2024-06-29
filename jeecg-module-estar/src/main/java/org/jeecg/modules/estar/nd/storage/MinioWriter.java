package org.jeecg.modules.estar.nd.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.jeecg.common.util.MinioUtil;

import org.jeecg.modules.estar.nd.file.WriteFile;
import org.jeecg.modules.estar.nd.file.Writer;
import org.jeecg.modules.estar.nd.util.EstarUtils;

public class MinioWriter extends Writer {

    private MinioConfig minioConfig;

    public MinioWriter(){

    }

    public MinioWriter(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {


        try {
            /*MinioClient minioClient =
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();*/
        	MinioClient minioClient = MinioUtil.getMinioClient();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if(!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(EstarUtils.getAliyunObjectNameByFileUrl(writeFile.getFileUrl())).stream(
                                    inputStream, inputStream.available(), -1)
//                            .contentType("video/mp4")
                            .build());

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

}
