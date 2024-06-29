package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RestoreFileDTO {
	@ApiModelProperty(value="删除批次号")
    private String deleteBatchNum;
	@ApiModelProperty(value="文件路径")
    private String filePath;
}

