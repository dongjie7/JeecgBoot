package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareListDTO {
	@ApiModelProperty(value="分享文件路径")
    private String shareFilePath;
	@ApiModelProperty(value="批次号")
    private String shareBatchNum;
	@ApiModelProperty(value = "当前页码")
    private Long currentPage;
	@ApiModelProperty(value = "一页显示数量")
    private Long pageCount;
}
