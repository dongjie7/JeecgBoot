package org.jeecg.modules.estar.nd.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.estar.nd.file.UploadFileResult;

import cn.hutool.core.util.IdUtil;

import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 网盘文件表
 * @Author: nbacheng
 * @Date:   2023-04-05
 * @Version: V1.0
 */
@Data
@TableName("nd_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_file对象", description="网盘文件表")
public class NdFile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建用户id*/
	@Excel(name = "创建用户id", width = 15)
    @ApiModelProperty(value = "创建用户id")
    private String createuserid;
	/**文件大小*/
	@Excel(name = "文件大小", width = 15)
    @ApiModelProperty(value = "文件大小")
    private Long filesize;
	/**文件状态(0-禁用，1-启用*/
	@Excel(name = "文件状态(0-禁用，1-启用", width = 15)
    @ApiModelProperty(value = "文件状态(0-禁用，1-启用")
    private Integer filestatus;
	/**文件url*/
	@Excel(name = "文件url", width = 15)
    @ApiModelProperty(value = "文件url")
    private String fileurl;
	/**md5唯一标识*/
	@Excel(name = "md5唯一标识", width = 15)
    @ApiModelProperty(value = "md5唯一标识")
    private String identifier;
	/**存储类型*/
	@Excel(name = "存储类型", width = 15)
    @ApiModelProperty(value = "存储类型")
    private String storagetype;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    
    public NdFile(){

    }
    
    public NdFile(UploadFileResult uploadFileResult) {
        this.id = IdUtil.getSnowflakeNextIdStr();
        this.fileurl = uploadFileResult.getFileUrl();
        this.filesize = uploadFileResult.getFileSize();
        this.filestatus = 1;
        this.storagetype = uploadFileResult.getStorageType().getName();
        this.identifier = uploadFileResult.getIdentifier();
        this.createTime = new Date();

    }

    public NdFile(String fileUrl, Long fileSize, String storageType, String identifier, String userId) {
        this.id = IdUtil.getSnowflakeNextIdStr();
        this.fileurl = fileUrl;
        this.filesize = fileSize;
        this.filestatus = 1;
        this.storagetype = storageType;
        this.identifier = identifier;
        this.createTime = new Date();
        this.createuserid = userId;

    }
}
