package org.jeecg.modules.estar.nd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareFileVO {
	@ApiModelProperty(value="批次号")
    private String shareBatchNum;
	@ApiModelProperty(value = "提取编码")
    private String extractionCode;
}
