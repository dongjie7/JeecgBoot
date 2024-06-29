package org.jeecg.modules.flowable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author: WangYuZhou
 * @create: 2022-09-08 14:35
 * @description:
 **/

@Data
@TableName("bpm_tool_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bpm_tool_data对象", description="流程案例数据表-后期新增表")
public class BpmToolData implements Serializable {

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
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

    /**所属流程定义id*/
    @Excel(name = "所属流程定义id", width = 15)
    @ApiModelProperty(value = "所属流程定义id")
    private String bpmId;

    /**所属流程案例id*/
    @Excel(name = "所属流程案例id", width = 15)
    @ApiModelProperty(value = "所属流程案例id")
    private String bpmInfoId;

    /**所属流程案例名称*/
    @Excel(name = "所属流程案例名称", width = 15)
    @ApiModelProperty(value = "所属流程案例名称")
    private String bpmInfoName;


    /**表单类型*/
    @Excel(name = "表单类型", width = 15, dicCode = "bpm_table_cate")
    @Dict(dicCode = "bpm_table_cate")
    @ApiModelProperty(value = "表单类型")
    private String tableCate;
    /**在线表单id*/
    @Excel(name = "在线表单id", width = 15)
    @ApiModelProperty(value = "在线表单id")
    private String onlineTableId;

    /**设计表单id*/
    @Excel(name = "设计表单id", width = 15)
    @ApiModelProperty(value = "设计表单id")
    private String desformTableId;

    /**公共数据id*/
    @Excel(name = "公共数据id", width = 15)
    @ApiModelProperty(value = "公共数据id")
    private String commonDataId;

}
