package org.jeecg.modules.estar.tw.dto;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*
* @author nbacheng
* @date 2023-06-29
*/

@Data
public class ProjectDto  implements Serializable {
	@ApiModelProperty(value = "项目ID")
    private String id;
	@ApiModelProperty(value = "组织编码")
    private String organizationId;
	@ApiModelProperty(value = "类型")
    private Integer type;
	@ApiModelProperty(value = "删除标志")
    private Integer deleted;
	@ApiModelProperty(value = "是否归档")
    private Integer archive;
	@ApiModelProperty(value = "成员ID")
    private String memberId;
	@ApiModelProperty(value = "页码")
    private Integer pageNo;
	@ApiModelProperty(value = "页大小")
    private Integer pageSize;
	
}
