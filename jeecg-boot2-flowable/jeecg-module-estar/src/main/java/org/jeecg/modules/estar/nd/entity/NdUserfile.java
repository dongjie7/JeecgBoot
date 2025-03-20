package org.jeecg.modules.estar.nd.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.estar.nd.util.EstarFile;

import cn.hutool.core.util.IdUtil;

import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 网盘用户文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Data
@TableName("nd_userfile")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_userfile对象", description="网盘用户文件表")
public class NdUserfile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**文件ID*/
	@Excel(name = "文件ID", width = 15)
    @ApiModelProperty(value = "文件ID")
    private String fileid;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
    private String filename;
	/**扩展名*/
	@Excel(name = "扩展名", width = 15)
    @ApiModelProperty(value = "扩展名")
    private String extendname;
	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
    @ApiModelProperty(value = "文件路径")
    private String filepath;
	/**是否是目录(0-否,1-是)*/
	@Excel(name = "是否是目录(0-否,1-是)", width = 15)
    @ApiModelProperty(value = "是否是目录(0-否,1-是)")
    private Integer isdir;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private Integer deleteflag;
	/**删除时间*/
	@Excel(name = "删除时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "删除时间")
    private Date deletetime;
	/**删除批次号*/
	@Excel(name = "删除批次号", width = 15)
    @ApiModelProperty(value = "删除批次号")
    private String deletebatchnum;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    
    public NdUserfile() {};
    public NdUserfile(EstarFile estarFile, String userId, String fileId) {
        this.id = IdUtil.getSnowflakeNextIdStr();
        this.createBy = userId;
        this.fileid = fileId;
        this.filepath = estarFile.getParent();
        this.filename = estarFile.getNameNotExtend();
        this.extendname = estarFile.getExtendName();
        this.isdir = estarFile.isDirectory() ? 1 : 0;
        this.createTime = new Date();
        this.deleteflag = 0;
    }
    public boolean isDirectory() {
        return this.isdir == 1;
    }

    public boolean isFile() {
        return this.isdir == 0;
    }
}
