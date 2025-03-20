package org.jeecg.modules.estar.nd.file;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import org.jeecg.modules.estar.nd.util.CharsetUtils;
import org.jeecg.modules.estar.nd.util.EstarUtils;

import cn.hutool.core.io.FileTypeUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Data
public abstract class Previewer {

    public ThumbImage thumbImage;

    protected abstract InputStream getInputStream(PreviewFile previewFile);

    public void imageThumbnailPreview(HttpServletResponse httpServletResponse, PreviewFile previewFile) {
        String fileUrl = previewFile.getFileUrl();
        boolean isVideo = EstarUtils.isVideoFile(FilenameUtils.getExtension(fileUrl));
        String thumbnailImgUrl = previewFile.getFileUrl();
        if (isVideo) {
            thumbnailImgUrl = fileUrl.replace("." + FilenameUtils.getExtension(fileUrl), ".jpg");
        }


        File cacheFile = EstarUtils.getCacheFile(thumbnailImgUrl);

        if (cacheFile.exists()) {
            FileInputStream fis = null;
            OutputStream outputStream = null;
            try {
                fis = new FileInputStream(cacheFile);
                outputStream = httpServletResponse.getOutputStream();
                IOUtils.copy(fis, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fis);
                IOUtils.closeQuietly(outputStream);
            }

        } else {
            InputStream inputstream = null;
            OutputStream outputStream = null;
            InputStream in = null;
            try {
                inputstream = getInputStream(previewFile);
                if (inputstream != null) {
                    outputStream = httpServletResponse.getOutputStream();
                    int thumbImageWidth = thumbImage.getWidth();
                    int thumbImageHeight = thumbImage.getHeight();
                    int width = thumbImageWidth == 0 ? 150 : thumbImageWidth;
                    int height = thumbImageHeight == 0 ? 150 : thumbImageHeight;
                    String type = FileTypeUtil.getType(getInputStream(previewFile));
                    boolean isImageFile = EstarUtils.isImageFile(type);
                    if (isVideo) {
                        in = VideoOperation.thumbnailsImage(inputstream, cacheFile, width, height);
                    } else if (isImageFile) {
                        in = ImageOperation.thumbnailsImageForScale(inputstream, cacheFile, 50);
                    } else {
                        in = inputstream;
                    }
                    IOUtils.copy(in, outputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(outputStream);
                if (previewFile.getOssClient() != null) {
                    previewFile.getOssClient().shutdown();
                }
            }


        }
    }

    public void imageOriginalPreview(HttpServletResponse httpServletResponse, PreviewFile previewFile) {

        InputStream inputStream = null;

        OutputStream outputStream = null;

        try {
            inputStream = getInputStream(previewFile);
            outputStream = httpServletResponse.getOutputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            bytes = CharsetUtils.convertTxtCharsetToUTF8(bytes, FilenameUtils.getExtension(previewFile.getFileUrl()));
            outputStream.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            if (previewFile.getOssClient() != null) {
                previewFile.getOssClient().shutdown();
            }
        }
    }


}
