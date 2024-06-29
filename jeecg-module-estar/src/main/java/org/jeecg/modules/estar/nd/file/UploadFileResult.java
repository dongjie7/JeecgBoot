package org.jeecg.modules.estar.nd.file;

import java.awt.image.BufferedImage;

import lombok.Data;

@Data
public class UploadFileResult {
    private String fileName;
    private String extendName;
    private Long fileSize;
    private String fileUrl;
    private String identifier;
    private StorageTypeEnum storageType;
    private UploadFileStatusEnum status;
    private BufferedImage bufferedImage;

}
