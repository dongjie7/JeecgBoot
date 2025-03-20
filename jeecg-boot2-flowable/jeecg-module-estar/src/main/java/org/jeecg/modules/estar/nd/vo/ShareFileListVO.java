package org.jeecg.modules.estar.nd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareFileListVO {
    @ApiModelProperty(value="文件id")
    private String fileId;
    @ApiModelProperty(value="文件时间戳姓名")
    private String timeStampName;
    @ApiModelProperty(value="文件url")
    private String fileUrl;
    @ApiModelProperty(value="文件大小")
    private Long fileSize;
    @ApiModelProperty(value="是否sso存储")
    @Deprecated
    private Integer isOSS;
    @ApiModelProperty(value="存储类型")
    private String storageType;
    @ApiModelProperty(value="用户文件id")
    private String userFileId;

    @ApiModelProperty(value="文件名")
    private String fileName;
    @ApiModelProperty(value="文件路径")
    private String filePath;
    @ApiModelProperty(value="文件扩展名")
    private String extendName;
    @ApiModelProperty(value="是否是目录 0-否， 1-是")
    private Integer isDir;
    @ApiModelProperty(value="上传时间")
    private String uploadTime;
    @ApiModelProperty(value="分享文件路径")
    private String shareFilePath;
    private String extractionCode;
    private String shareBatchNum;
}
