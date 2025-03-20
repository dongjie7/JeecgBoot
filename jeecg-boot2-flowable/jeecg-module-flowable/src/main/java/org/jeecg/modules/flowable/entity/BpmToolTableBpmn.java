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
 * @create: 2022-08-27 11:40
 * @description: 流程定义与表单配置实体类
 **/


@Data
@TableName("bpm_tool_table_bpmn")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bpm_tool_table_bpmn对象", description="流程定义与表单配置表")
public class BpmToolTableBpmn implements Serializable {

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

    /**所属流程定义key*/
    @Excel(name = "所属流程定义key", width = 15)
    @ApiModelProperty(value = "所属流程定义key")
    private String bpmKey;

    /**部署id*/
    @Excel(name = "部署id", width = 15)
    @ApiModelProperty(value = "部署id")
    private String deployId;


    /**表单类型*/
    @Excel(name = "表单类型", width = 15, dicCode = "bpm_table_cate")
    @Dict(dicCode = "bpm_table_cate")
    @ApiModelProperty(value = "表单类型")
    private String tableCate;
    /**在线表单id*/
    @Excel(name = "在线表单id", width = 15)
    @ApiModelProperty(value = "在线表单id")
    private String onlineTableId;
    /**在线表单编码*/
    @Excel(name = "在线表单编码", width = 15)
    @ApiModelProperty(value = "在线表单编码")
    private String onlineTableCode;
    /**在线表单所属流程字段id*/
    @Excel(name = "在线表单所属流程字段id", width = 15)
    @ApiModelProperty(value = "在线表单所属流程字段id")
    private String onlineTableBpm;

    /**设计表单id*/
    @Excel(name = "设计表单id", width = 15)
    @ApiModelProperty(value = "设计表单id")
    private String desformTableId;
    /**设计表单编码*/
    @Excel(name = "设计表单编码", width = 15)
    @ApiModelProperty(value = "设计表单编码")
    private String desformTableCode;
    /**设计表单流程状态字段*/
    @Excel(name = "设计表单流程状态字段", width = 15)
    @ApiModelProperty(value = "设计表单流程状态字段")
    private String desformTableBpm;

    /**业务标题*/
    @Excel(name = "业务标题", width = 15)
    @ApiModelProperty(value = "业务标题")
    private String workTitle;

    /**发布状态*/
    @Excel(name = "发布状态", width = 15, dicCode = "bpm_publish_state")
    @Dict(dicCode = "bpm_publish_state")
    @ApiModelProperty(value = "发布状态")
    private String publishState;

}
