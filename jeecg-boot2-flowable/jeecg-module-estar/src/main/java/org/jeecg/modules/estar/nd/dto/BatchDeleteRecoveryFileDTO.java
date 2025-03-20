package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BatchDeleteRecoveryFileDTO {
	@ApiModelProperty(value="用户文件Id集合", required = true)
    private String userFileIds;
}