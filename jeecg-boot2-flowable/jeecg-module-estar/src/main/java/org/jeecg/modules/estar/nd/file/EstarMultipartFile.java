package org.jeecg.modules.estar.nd.file;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import org.jeecg.modules.estar.nd.util.EstarUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class EstarMultipartFile {

    MultipartFile multipartFile = null;

    private EstarMultipartFile() {
    }

    public EstarMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getFileName() {
        String originalName = getMultipartFile().getOriginalFilename();
        if (!originalName.contains(".")) {
            return originalName;
        }
        return originalName.substring(0, originalName.lastIndexOf("."));
    }

    public String getExtendName() {
        String originalName = getMultipartFile().getOriginalFilename();
        return FilenameUtils.getExtension(originalName);
    }

    public String getFileUrl() {
        String uuid = UUID.randomUUID().toString();
        return EstarUtils.getUploadFileUrl(uuid, getExtendName());
    }

    public String getFileUrl(String identify) {
        return EstarUtils.getUploadFileUrl(identify, getExtendName());
    }

    public InputStream getUploadInputStream() throws IOException {
        return getMultipartFile().getInputStream();
    }

    public byte[] getUploadBytes() throws IOException {
        return getMultipartFile().getBytes();
    }

    public long getSize() {
        return getMultipartFile().getSize();
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

}
