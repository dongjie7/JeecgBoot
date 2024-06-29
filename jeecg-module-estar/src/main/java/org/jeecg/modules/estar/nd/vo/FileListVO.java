package org.jeecg.modules.estar.nd.vo;


import lombok.Data;

@Data
public class FileListVO {
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

    private Integer imageWidth;
    private Integer imageHeight;

}
