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
 * @Description: nd_picturefile
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Data
@TableName("nd_picturefile")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_picturefile对象", description="nd_picturefile")
public class NdPicturefile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建用户*/
    @ApiModelProperty(value = "创建用户")
    private String createBy;
    /**创建用户id*/
	@Excel(name = "创建用户id", width = 15)
    @ApiModelProperty(value = "创建用户id")
    private String createuserid;
	/**扩展名*/
	@Excel(name = "扩展名", width = 15)
    @ApiModelProperty(value = "扩展名")
    private String extendname;
	/**文件名*/
	@Excel(name = "文件名", width = 15)
    @ApiModelProperty(value = "文件名")
    private String filename;
	/**文件大小*/
	@Excel(name = "文件大小", width = 15)
    @ApiModelProperty(value = "文件大小")
    private Integer filesize;
	/**文件url*/
	@Excel(name = "文件url", width = 15)
    @ApiModelProperty(value = "文件url")
    private String fileurl;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**修改用户id*/
    @ApiModelProperty(value = "修改用户id")
    private String updateBy;
	/**存储类型*/
	@Excel(name = "存储类型", width = 15)
    @ApiModelProperty(value = "存储类型")
    private String storagetype;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userid;
}
