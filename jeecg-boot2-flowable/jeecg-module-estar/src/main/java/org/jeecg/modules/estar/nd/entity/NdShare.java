package org.jeecg.modules.estar.nd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 网盘分享表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Data
@TableName("nd_share")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_share对象", description="网盘分享表")
public class NdShare implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**失效时间*/
	@Excel(name = "失效时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "失效时间")
    private java.util.Date endtime;
	/**提取码*/
	@Excel(name = "提取码", width = 15)
    @ApiModelProperty(value = "提取码")
    private String extractioncode;
	/**分享状态(0正常,1已失效,2已撤销)*/
	@Excel(name = "分享状态(0正常,1已失效,2已撤销)", width = 15)
    @ApiModelProperty(value = "分享状态(0正常,1已失效,2已撤销)")
    private Integer sharestatus;
	/**分享类型(0公共,1私密,2好友)*/
	@Excel(name = "分享类型(0公共,1私密,2好友)", width = 15)
    @ApiModelProperty(value = "分享类型(0公共,1私密,2好友)")
    private Integer sharetype;
	/**分享批次号*/
	@Excel(name = "分享批次号", width = 15)
    @ApiModelProperty(value = "分享批次号")
    private String sharebatchnum;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
