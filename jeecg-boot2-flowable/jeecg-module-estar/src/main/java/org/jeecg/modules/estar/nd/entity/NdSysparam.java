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
 * @Description: nd_sysparam
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
@Data
@TableName("nd_sysparam")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="nd_sysparam对象", description="nd_sysparam")
public class NdSysparam implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**groupname*/
	@Excel(name = "groupname", width = 15)
    @ApiModelProperty(value = "groupname")
    private String groupname;
	/**sysparamdesc*/
	@Excel(name = "sysparamdesc", width = 15)
    @ApiModelProperty(value = "sysparamdesc")
    private String sysparamdesc;
	/**sysparamkey*/
	@Excel(name = "sysparamkey", width = 15)
    @ApiModelProperty(value = "sysparamkey")
    private String sysparamkey;
	/**sysparamvalue*/
	@Excel(name = "sysparamvalue", width = 15)
    @ApiModelProperty(value = "sysparamvalue")
    private String sysparamvalue;
}
