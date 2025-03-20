package org.jeecg.modules.estar.oa.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMessageVo {
	/**ID*/
	@ApiModelProperty(value = "ID")
	private String Id;
	/**推送内容*/
	@ApiModelProperty(value = "推送内容")
	private String esContent;
	/**接收人*/
	@ApiModelProperty(value = "接收人")
	private String esReceiver;
	/**发送次数*/
	@ApiModelProperty(value = "发送次数")
	private Integer esSendNum;
	/**推送状态 0未推送 1推送成功 2推送失败*/
	@ApiModelProperty(value = "推送状态 0未推送 1推送成功 2推送失败")
	private String esSendStatus;
	/**推送时间*/
	@ApiModelProperty(value = "推送时间")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date esSendTime;
	/**消息标题*/
	@ApiModelProperty(value = "消息标题")
	private String esTitle;
	/**推送方式：1短信 2邮件 3微信 4websocket消息*/
	@ApiModelProperty(value = "推送方式：1短信 2邮件 3微信 4websocket消息")
	private String esType;
	/**备注*/
	@ApiModelProperty(value = "备注")
	private String remark;
}
