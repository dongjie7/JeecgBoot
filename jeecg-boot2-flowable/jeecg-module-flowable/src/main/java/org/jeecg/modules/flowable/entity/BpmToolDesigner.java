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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author: WangYuZhou
 * @create: 2022-09-18 10:15
 * @description: 表单字段控件类型与前端插件编码关系
 **/

@Data
@TableName("bpm_tool_designer")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bpm_tool_designer对象", description="表单控件与前端编码的匹配关系")
public class BpmToolDesigner implements Serializable {

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

    /**表字段控件类型*/
    @Excel(name = "表字段控件类型", width = 15)
    @ApiModelProperty(value = "表字段控件类型")
    private String fieldShowType;

    /**前端控件编码*/
    @Excel(name = "前端控件编码", width = 15)
    @ApiModelProperty(value = "前端控件编码")
    private String fieldType;


}
