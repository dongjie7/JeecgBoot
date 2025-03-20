package org.jeecg.modules.estar.nd.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import org.jeecg.modules.estar.nd.util.EstarUtils;

import java.io.File;

@Slf4j
public abstract class Deleter {
    public abstract void delete(DeleteFile deleteFile);

    protected void deleteCacheFile(DeleteFile deleteFile) {
        if (EstarUtils.isImageFile(FilenameUtils.getExtension(deleteFile.getFileUrl()))) {
            File cacheFile = EstarUtils.getCacheFile(deleteFile.getFileUrl());
            if (cacheFile.exists()) {
                boolean result = cacheFile.delete();
                if (!result) {
                    log.error("删除本地缓存文件失败！");
                }
            }
        }
    }
}
