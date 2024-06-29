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
 * @Description: bs_data_set
 * @Author: nbacheng
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Data
@TableName("bs_data_set")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_data_set对象", description="bs_data_set")
public class BsDataSet implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**数据集编码*/
	@Excel(name = "数据集编码", width = 15)
    @ApiModelProperty(value = "数据集编码")
    private String setCode;
	/**数据集名称*/
	@Excel(name = "数据集名称", width = 15)
    @ApiModelProperty(value = "数据集名称")
    private String setName;
	/**数据集描述*/
	@Excel(name = "数据集描述", width = 15)
    @ApiModelProperty(value = "数据集描述")
    private String setDesc;
	/**数据源编码*/
	@Excel(name = "数据源编码", width = 15, dictTable = "bs_data_source", dicText = "code", dicCode = "code")
	@Dict(dictTable = "bs_data_source", dicText = "code", dicCode = "code")
    @ApiModelProperty(value = "数据源编码")
    private String sourceCode;
	/**查询sq*/
	@Excel(name = "查询sq", width = 15)
    @ApiModelProperty(value = "查询sq")
    private String dynSentence;
	/**数据集类型*/
	@Excel(name = "数据集类型", width = 15)
    @ApiModelProperty(value = "数据集类型")
    private String setType;
	/**结果案例*/
	@Excel(name = "结果案例", width = 15)
    @ApiModelProperty(value = "结果案例")
    private String caseResult;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "erp_status")
	@Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private Integer status;
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
