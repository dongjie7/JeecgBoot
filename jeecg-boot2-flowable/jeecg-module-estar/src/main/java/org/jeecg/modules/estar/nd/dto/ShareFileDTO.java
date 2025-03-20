package org.jeecg.modules.estar.nd.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareFileDTO {
	@ApiModelProperty(value="文件集合")
    private String files;
	@ApiModelProperty(value = "过期日期", example="2020-05-23 22:10:33")
    private Date endtime;
	@ApiModelProperty(value = "分享类型", example="0公共分享，1私密分享，2好友分享")
    private Integer sharetype;
	@ApiModelProperty(value = "备注", example="")
    private String remarks;
}