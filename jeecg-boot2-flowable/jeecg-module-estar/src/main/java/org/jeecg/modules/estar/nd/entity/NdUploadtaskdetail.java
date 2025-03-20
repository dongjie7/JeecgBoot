package org.jeecg.modules.estar.nd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: nd_uploadtaskdetail
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Data
@TableName("nd_uploadtaskdetail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_uploadtaskdetail对象", description="nd_uploadtaskdetail")
public class NdUploadtaskdetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**当前分片数*/
	@Excel(name = "当前分片数", width = 15)
    @ApiModelProperty(value = "当前分片数")
    private Integer chunknumber;
	/**当前分片大小*/
	@Excel(name = "当前分片大小", width = 15)
    @ApiModelProperty(value = "当前分片大小")
    private Integer chunksize;
	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
    @ApiModelProperty(value = "文件路径")
    private String filepath;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
    private String filename;
	/**文件md5唯一标识*/
	@Excel(name = "文件md5唯一标识", width = 15)
    @ApiModelProperty(value = "文件md5唯一标识")
    private String identifier;
	/**文件相对路径*/
	@Excel(name = "文件相对路径", width = 15)
    @ApiModelProperty(value = "文件相对路径")
    private String relativepath;
	/**文件总分片数*/
	@Excel(name = "文件总分片数", width = 15)
    @ApiModelProperty(value = "文件总分片数")
    private Integer totalchunks;
	/**文件总大小*/
	@Excel(name = "文件总大小", width = 15)
    @ApiModelProperty(value = "文件总大小")
    private Integer totalsize;
}
