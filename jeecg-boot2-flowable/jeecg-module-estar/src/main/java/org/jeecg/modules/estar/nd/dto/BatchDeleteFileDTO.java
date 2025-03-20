package org.jeecg.modules.estar.nd.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BatchDeleteFileDTO {
	@ApiModelProperty(value="文件Id集合", required = true)
    @NotEmpty
    private String userFileIds;

}