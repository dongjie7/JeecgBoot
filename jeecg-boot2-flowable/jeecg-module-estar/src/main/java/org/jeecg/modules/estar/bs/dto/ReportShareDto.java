
package org.jeecg.modules.estar.bs.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.jeecg.common.system.base.entity.JeecgEntity;

import java.util.Date;

/**
*
* @description 报表分享 dto
* @author nbacheng
* @date 2023-03-28
**/
@Data
public class ReportShareDto extends JeecgEntity implements Serializable {
    /** 分享编码，系统生成，默认UUID */
    @ApiModelProperty(value = "分享编码，系统生成，默认UUID")
    private String shareCode;

    /** 分享有效期类型，DIC_NAME=BS_SHARE_VAILD */
    @ApiModelProperty(value = "分享有效期类型，DIC_NAME=BS_SHARE_VAILD")
    @NotNull(message = "6002")
    private Integer shareValidType;

    /** 分享有效期 */
    @ApiModelProperty(value = "分享有效期")
    private Date shareValidTime;

    /** 分享token */
    @ApiModelProperty(value = "分享token")
    private String shareToken;

    /** 分享url */
    @ApiModelProperty(value = "分享url")
    @NotEmpty(message = "6002")
    private String shareUrl;

    /** 报表编码 */
    @ApiModelProperty(value = "报表编码")
    @NotEmpty(message = "6002")
    private String reportCode;

    /** 0--禁用 1--启用  */
    @ApiModelProperty(value = "0--禁用 1--启用  ")
    private String status;


    /** 分享码 */
    private String sharePassword;

    private boolean sharePasswordFlag = false;

    private String reportType;

}
