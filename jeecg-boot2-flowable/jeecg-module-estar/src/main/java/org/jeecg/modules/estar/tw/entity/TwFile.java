package org.jeecg.modules.estar.tw.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 项目文件表
 * @Author: nbacheng
 * @Date:   2023-07-11
 * @Version: V1.0
 */
@Data
@TableName("tw_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tw_file对象", description="项目文件表")
public class TwFile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private String id;
	/**相对路径*/
	@Excel(name = "相对路径", width = 15)
    @ApiModelProperty(value = "相对路径")
    private String pathName;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String title;
	/**扩展名*/
	@Excel(name = "扩展名", width = 15)
    @ApiModelProperty(value = "扩展名")
    private String extension;
	/**文件大小*/
	@Excel(name = "文件大小", width = 15)
    @ApiModelProperty(value = "文件大小")
	@TableField("file_size")
    private long fileSize;
	/**对象类型*/
	@Excel(name = "对象类型", width = 15)
    @ApiModelProperty(value = "对象类型")
    private String objectType;
	/**组织编码*/
	@Excel(name = "组织编码", width = 15)
    @ApiModelProperty(value = "组织编码")
    private String organizationId;
	/**任务编码*/
	@Excel(name = "任务编码", width = 15)
    @ApiModelProperty(value = "任务编码")
    private String taskId;
	/**项目编码*/
	@Excel(name = "项目编码", width = 15)
    @ApiModelProperty(value = "项目编码")
    private String projectId;
	/**上传人*/
    @ApiModelProperty(value = "上传人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**下载次数*/
	@Excel(name = "下载次数", width = 15)
    @ApiModelProperty(value = "下载次数")
	@TableField("downloads")
    private long downloads;
	/**额外信息*/
	@Excel(name = "额外信息", width = 15)
    @ApiModelProperty(value = "额外信息")
    private String extra;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private Integer deleted;
	/**完整地址*/
	@Excel(name = "完整地址", width = 15)
    @ApiModelProperty(value = "完整地址")
    private String fileUrl;
	/**文件类型*/
	@Excel(name = "文件类型", width = 15)
    @ApiModelProperty(value = "文件类型")
    private String fileType;
	/**删除时间*/
	@Excel(name = "删除时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "删除时间")
    private Date deletedTime;
	
	@TableField(exist = false)
    private String creatorName;
    @TableField(exist = false)
    private String fullName;
}
