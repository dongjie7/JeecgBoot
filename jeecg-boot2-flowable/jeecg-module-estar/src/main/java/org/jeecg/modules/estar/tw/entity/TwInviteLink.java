package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 项目邀请链接表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@Data
@TableName("tw_invite_link")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_invite_link对象", description="项目邀请链接表")
public class TwInviteLink implements Serializable {
    private static final long serialVersionUID = 1L;

	/**邀请码*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "邀请码")
    private String id;
	/**邀请人*/
    @ApiModelProperty(value = "邀请人")
    private String createBy;
	/**链接类型*/
	@Excel(name = "链接类型", width = 15)
    @ApiModelProperty(value = "链接类型")
    private String inviteType;
	/**资源编码*/
	@Excel(name = "资源编码", width = 15)
    @ApiModelProperty(value = "资源编码")
    private String sourceId;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**过期时间*/
	@Excel(name = "过期时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "过期时间")
    private Date overTime;
}
