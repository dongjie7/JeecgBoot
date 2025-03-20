package org.jeecg.modules.estar.nd.storage;

import org.apache.commons.io.FilenameUtils;

import org.jeecg.modules.estar.nd.exception.ReadException;
import org.jeecg.modules.estar.nd.file.ReadFile;
import org.jeecg.modules.estar.nd.file.Reader;
import org.jeecg.modules.estar.nd.util.EstarUtils;
import org.jeecg.modules.estar.nd.util.ReadFileUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class LocalStorageReader extends Reader {
    @Override
    public String read(ReadFile readFile) {

        String fileContent;
        try {
            String extendName = FilenameUtils.getExtension(readFile.getFileUrl());
            FileInputStream fileInputStream = new FileInputStream(EstarUtils.getStaticPath() + readFile.getFileUrl());
            fileContent = ReadFileUtils.getContentByInputStream(extendName, fileInputStream);
        } catch (IOException e) {
            throw new ReadException("文件读取出现异常", e);
        }
        return fileContent;
    }
}
