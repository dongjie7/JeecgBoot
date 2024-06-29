package org.jeecg.modules.estar.nd.file;

import lombok.Data;

@Data
    public class UploadFileInfo {
        private String bucketName;
        private String key;
        private String uploadId;
    }