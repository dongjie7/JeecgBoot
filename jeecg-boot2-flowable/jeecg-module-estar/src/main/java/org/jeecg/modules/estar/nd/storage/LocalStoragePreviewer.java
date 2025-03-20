package org.jeecg.modules.estar.nd.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jeecg.modules.estar.nd.file.PreviewFile;
import org.jeecg.modules.estar.nd.file.Previewer;
import org.jeecg.modules.estar.nd.file.ThumbImage;
import org.jeecg.modules.estar.nd.util.EstarUtils;

public class LocalStoragePreviewer extends Previewer {

    public LocalStoragePreviewer(){

    }
    public LocalStoragePreviewer(ThumbImage thumbImage) {
        setThumbImage(thumbImage);
    }

    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        //设置文件路径
        File file = EstarUtils.getLocalSaveFile(previewFile.getFileUrl());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return inputStream;

    }

}
