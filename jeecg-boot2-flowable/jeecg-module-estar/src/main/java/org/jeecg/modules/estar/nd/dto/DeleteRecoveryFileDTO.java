package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteRecoveryFileDTO {
	@ApiModelProperty(value = "用户文件id", required = true)
    private String userFileId;

}