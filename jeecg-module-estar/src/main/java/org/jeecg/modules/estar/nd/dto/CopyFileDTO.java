package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CopyFileDTO {
	@ApiModelProperty(value = "用户文件id集合", required = true)
    private String userFileIds;
	@ApiModelProperty(value = "文件路径", required = true)
    private String filePath;
}

