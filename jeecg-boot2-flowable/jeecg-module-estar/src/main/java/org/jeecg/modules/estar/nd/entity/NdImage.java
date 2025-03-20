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
 * @Description: 网盘图像表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
@Data
@TableName("nd_image")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_image对象", description="网盘图像表")
public class NdImage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**文件ID*/
	@Excel(name = "文件ID", width = 15)
    @ApiModelProperty(value = "文件ID")
    private String fileid;
	/**图像的高*/
	@Excel(name = "图像的高", width = 15)
    @ApiModelProperty(value = "图像的高")
    private Integer imageheight;
	/**图像的宽*/
	@Excel(name = "图像的宽", width = 15)
    @ApiModelProperty(value = "图像的宽")
    private Integer imagewidth;
}
