package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareFileListDTO {
	@ApiModelProperty(value="批次号")
    private String shareBatchNum;
	@ApiModelProperty(value="分享文件路径")
    private String shareFilePath;
}