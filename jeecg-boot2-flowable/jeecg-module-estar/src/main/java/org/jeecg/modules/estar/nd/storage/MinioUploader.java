package org.jeecg.modules.estar.nd.storage;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jeecg.common.util.MinioUtil;
import org.jeecg.common.util.RedisUtil;

import org.jeecg.modules.estar.nd.file.EstarMultipartFile;
import org.jeecg.modules.estar.nd.file.StorageTypeEnum;
import org.jeecg.modules.estar.nd.file.UploadFile;
import org.jeecg.modules.estar.nd.file.UploadFileResult;
import org.jeecg.modules.estar.nd.file.UploadFileStatusEnum;
import org.jeecg.modules.estar.nd.file.Uploader;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.UploadException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MinioUploader extends Uploader {

    private MinioConfig minioConfig;

    @Resource
    RedisUtil redisUtil;

    public MinioUploader(){

    }

    public MinioUploader(MinioConfig minioConfig){
        this.minioConfig = minioConfig;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
    }

    @Override
    protected void doUploadFileChunk(EstarMultipartFile estarMultipartFile, UploadFile uploadFile) {

    }

    @Override
    protected UploadFileResult organizationalResults(EstarMultipartFile estarMultipartFile, UploadFile uploadFile) {
        return null;
    }

    @Override
    protected UploadFileResult doUploadFlow(EstarMultipartFile estarMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            estarMultipartFile.getFileUrl(uploadFile.getIdentifier());
            String fileUrl = EstarUtils.getUploadFileUrl(uploadFile.getIdentifier(), estarMultipartFile.getExtendName());

            File tempFile =  EstarUtils.getTempFile(fileUrl);
            File processFile = EstarUtils.getProcessFile(fileUrl);

            byte[] fileData = estarMultipartFile.getUploadBytes();

            writeByteDataToFile(fileData, tempFile, uploadFile);

            //判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, processFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(estarMultipartFile.getFileName());
            uploadFileResult.setExtendName(estarMultipartFile.getExtendName());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnum.MINIO);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(estarMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {

                minioUpload(fileUrl, tempFile, uploadFile);
                uploadFileResult.setFileUrl(fileUrl);
                tempFile.delete();

                if (EstarUtils.isImageFile(uploadFileResult.getExtendName())) {
                    InputStream inputStream = null;
                    try {
                    	
                    	MinioClient minioClient = MinioUtil.getMinioClient();
                        inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(uploadFileResult.getFileUrl()).build());

                        BufferedImage src  = ImageIO.read(inputStream);
                        uploadFileResult.setBufferedImage(src);
                    } catch (IOException | InternalException | XmlParserException | InvalidResponseException | InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException | ServerException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }

                }

                uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
            } else {
                uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLATE);
            }
        } catch (IOException e) {
            throw new UploadException(e);
        }


        return uploadFileResult;
    }


    private void minioUpload(String fileUrl, File file,  UploadFile uploadFile) {
        InputStream inputStream = null;
        try {
            /*MinioClient minioClient = 
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();*/
        	MinioClient minioClient =  MinioUtil.getMinioClient();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if(!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            inputStream = new FileInputStream(file);
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileUrl).stream(
                                    inputStream, uploadFile.getTotalSize(), 1024 * 1024 * 5)
//                            .contentType("video/mp4")
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }


}
