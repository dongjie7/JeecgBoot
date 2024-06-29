package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BatchMoveFileDTO {
	@ApiModelProperty(value="用户文件Id集合", required = true)
    private String userFileIds;
	@ApiModelProperty(value="目的文件路径", required = true)
    private String filePath;
}