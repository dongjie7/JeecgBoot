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
 * @Description: 大屏Excel报表
 * @Author: nbacheng
 * @Date:   2023-03-23
 * @Version: V1.0
 */
@Data
@TableName("bs_report_excel")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_report_excel对象", description="大屏Excel报表")
public class BsReportExcel implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**报表编码*/
	@Excel(name = "报表编码", width = 15)
    @ApiModelProperty(value = "报表编码")
    private String reportCode;
	/**数据集编码，以|分割*/
	@Excel(name = "数据集编码，以|分割", width = 15)
    @ApiModelProperty(value = "数据集编码，以|分割")
    private String setCodes;
	/**数据集查询参数*/
	@Excel(name = "数据集查询参数", width = 15)
    @ApiModelProperty(value = "数据集查询参数")
    private String setParam;
	/**报表json串*/
	@Excel(name = "报表json串", width = 15)
    @ApiModelProperty(value = "报表json串")
    private String jsonStr;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "erp_status")
	@Dict(dicCode = "erp_status")
    @ApiModelProperty(value = "状态")
    private String status;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
}
