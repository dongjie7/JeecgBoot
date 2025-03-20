package org.jeecg.modules.estar.nd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchFileDTO {
	@ApiModelProperty(value = "文件名", required = true)
    private String fileName;
	@ApiModelProperty(value = "当前页", required = true)
    private Integer currentPage;
	@ApiModelProperty(value = "每页数量", required = true)
    private Integer pageCount;
}