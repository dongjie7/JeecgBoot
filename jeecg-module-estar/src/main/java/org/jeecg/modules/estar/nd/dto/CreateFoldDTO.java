package org.jeecg.modules.estar.nd.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.jeecg.modules.estar.nd.constant.RegexConstant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateFoldDTO {
	@ApiModelProperty(value="文件名", required=true)
    @NotBlank(message = "文件名不能为空")
    @Pattern(regexp = RegexConstant.FILE_NAME_REGEX, message = "文件名不合法！")
    private String fileName;
	@ApiModelProperty(value="文件路径", required=true)
    private String filePath;

}