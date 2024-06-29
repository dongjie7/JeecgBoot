package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateFileDTO {
	@ApiModelProperty(value = "用户文件id")
    private String userFileId;
	@ApiModelProperty(value = "文件内容")
    private String fileContent;
}
