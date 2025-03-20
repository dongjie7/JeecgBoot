package org.jeecg.modules.estar.nd.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BatchDownloadFileDTO {
	@ApiModelProperty(value="文件集合", required = true)
    private String userFileIds;

}