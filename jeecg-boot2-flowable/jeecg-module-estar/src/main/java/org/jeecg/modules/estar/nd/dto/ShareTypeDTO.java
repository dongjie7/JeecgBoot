package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareTypeDTO {

	@ApiModelProperty(value="批次号")
    private String shareBatchNum;


}