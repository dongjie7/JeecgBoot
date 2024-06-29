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
 * @Description: nd_filetype
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Data
@TableName("nd_filetype")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_filetype对象", description="nd_filetype")
public class NdFiletype implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**文件类型名*/
	@Excel(name = "文件类型名", width = 15)
    @ApiModelProperty(value = "文件类型名")
    private String filetypename;
	/**次序*/
	@Excel(name = "次序", width = 15)
    @ApiModelProperty(value = "次序")
    private Integer ordernum;
}
