package org.jeecg.modules.estar.nd.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.jeecg.modules.estar.nd.file.DownloadFile;
import org.jeecg.modules.estar.nd.file.Downloader;
import org.jeecg.modules.estar.nd.util.EstarUtils;

import java.io.*;

@Slf4j
@Component
public class LocalStorageDownloader extends Downloader {

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {
        //设置文件路径
        File file = new File(EstarUtils.getStaticPath() + downloadFile.getFileUrl());

        InputStream inputStream = null;
        try {
            if (downloadFile.getRange() != null) {
                RandomAccessFile randowAccessFile = new RandomAccessFile(file, "r");
                randowAccessFile.seek(downloadFile.getRange().getStart());
                byte[] bytes = new byte[downloadFile.getRange().getLength()];
                randowAccessFile.read(bytes);
                inputStream = new ByteArrayInputStream(bytes);
            } else {
                inputStream = new FileInputStream(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;

    }
}
