package org.jeecg.modules.estar.nd.storage;

import org.springframework.stereotype.Component;

import org.jeecg.modules.estar.nd.exception.DeleteException;
import org.jeecg.modules.estar.nd.file.DeleteFile;
import org.jeecg.modules.estar.nd.file.Deleter;
import org.jeecg.modules.estar.nd.util.EstarUtils;

import java.io.File;

@Component
public class LocalStorageDeleter extends Deleter {
    @Override
    public void delete(DeleteFile deleteFile) {
        File localSaveFile = EstarUtils.getLocalSaveFile(deleteFile.getFileUrl());
        if (localSaveFile.exists()) {
            boolean result = localSaveFile.delete();
            if (!result) {
                throw new DeleteException("删除本地文件失败");
            }
        }

        deleteCacheFile(deleteFile);
    }
}
