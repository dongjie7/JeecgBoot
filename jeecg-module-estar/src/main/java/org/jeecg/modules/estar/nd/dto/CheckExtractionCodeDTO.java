package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CheckExtractionCodeDTO {
	@ApiModelProperty(value="批次号")
    private String shareBatchNum;
	@ApiModelProperty(value="提取码")
    private String extractionCode;
}