package org.jeecg.modules.estar.nd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareTypeVO {
	@ApiModelProperty(value="0公共，1私密，2好友")
    private Integer shareType;
}
