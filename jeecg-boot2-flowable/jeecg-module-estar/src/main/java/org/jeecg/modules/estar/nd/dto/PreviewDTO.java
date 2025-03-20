package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PreviewDTO {
    private String userFileId;
    @ApiModelProperty(value="批次号")
    private String shareBatchNum;
    @ApiModelProperty(value="提取码")
    private String extractionCode;
    private String isMin;
    private Integer platform;
    private String url;
    private String token;
}
