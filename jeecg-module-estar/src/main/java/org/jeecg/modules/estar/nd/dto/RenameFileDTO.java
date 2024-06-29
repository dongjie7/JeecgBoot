package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RenameFileDTO {
	@ApiModelProperty(value = "用户文件id", required = true)
    private String userFileId;

	@ApiModelProperty(value = "文件名", required = true)
    private String fileName;
}