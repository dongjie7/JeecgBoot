package org.jeecg.modules.estar.nd.vo;

import org.jeecg.modules.estar.nd.entity.NdImage;
import org.jeecg.modules.estar.nd.entity.NdMusic;

import lombok.Data;

@Data
public class FileDetailVO {
    private String fileId;

    private String timeStampName;

    private String fileUrl;

    private Long fileSize;

    private String storageType;

    private Integer pointCount;

    private String identifier;

    private String userFileId;

    private String userId;


    private String fileName;

    private String filePath;

    private String extendName;

    private Integer isDir;

    private String uploadTime;

    private Integer deleteFlag;

    private String deleteTime;

    private String deleteBatchNum;

    private NdImage image;

    private NdMusic music;
}
