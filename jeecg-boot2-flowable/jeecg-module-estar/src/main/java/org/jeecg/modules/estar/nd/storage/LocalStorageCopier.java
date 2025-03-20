package org.jeecg.modules.estar.nd.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.jeecg.modules.estar.nd.exception.CopyException;
import org.jeecg.modules.estar.nd.file.Copier;
import org.jeecg.modules.estar.nd.file.CopyFile;
import org.jeecg.modules.estar.nd.util.EstarUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
public class LocalStorageCopier extends Copier {
    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = EstarUtils.getUploadFileUrl(uuid, copyFile.getExtendName());
        File saveFile = new File(EstarUtils.getStaticPath() + fileUrl);
        try {
            FileUtils.copyInputStreamToFile(inputStream, saveFile);
        } catch (IOException e) {
            throw new CopyException("创建文件出现异常", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return fileUrl;
    }
}
