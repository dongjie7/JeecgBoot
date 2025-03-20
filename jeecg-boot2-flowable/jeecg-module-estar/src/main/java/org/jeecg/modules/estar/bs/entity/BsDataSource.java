package org.jeecg.modules.estar.bs.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: bs_data_source
 * @Author: nbacheng
 * @Date:   2023-03-14
 * @Version: V1.0
 */
@Data
@TableName("bs_data_source")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_data_source对象", description="bs_data_source")
public class BsDataSource implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**数据源编码*/
	@Excel(name = "数据源编码", width = 15)
    @ApiModelProperty(value = "数据源编码")
    private String code;
	/**数据源名称*/
	@Excel(name = "数据源名称", width = 15)
    @ApiModelProperty(value = "数据源名称")
    private String name;
	/**数据源类型*/
	@Excel(name = "数据源类型", width = 15, dictTable = "bs_source_type", dicText = "name", dicCode = "code")
	@Dict(dictTable = "bs_source_type", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "数据源类型")
    private String type;
	/**数据源配置*/
	@Excel(name = "数据源配置", width = 15)
    @ApiModelProperty(value = "数据源配置")
    private String config;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
