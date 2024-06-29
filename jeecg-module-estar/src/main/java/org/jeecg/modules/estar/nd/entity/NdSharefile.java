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
 * @Description: 网盘分享文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Data
@TableName("nd_sharefile")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_sharefile对象", description="网盘分享文件表")
public class NdSharefile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**分享批次号*/
	@Excel(name = "分享批次号", width = 15)
    @ApiModelProperty(value = "分享批次号")
    private String sharebatchnum;
	/**分享文件路径*/
	@Excel(name = "分享文件路径", width = 15)
    @ApiModelProperty(value = "分享文件路径")
    private String sharefilepath;
	/**用户文件id*/
	@Excel(name = "用户文件id", width = 15)
    @ApiModelProperty(value = "用户文件id")
    private String userfileid;
}
