package org.jeecg.modules.flowable.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author: WangYuZhou
 * @create: 2022-08-27 14:45
 * @description:
 **/

@Data
@TableName("bpm_tool_table_bpmn")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bpm_tool_table_bpmn_info对象", description="流程定义与表单配置表")
public class BpmToolTableBpmnVO implements Serializable {

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

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

    /**表单id*/
    @Excel(name = "表单id", width = 15)
    @ApiModelProperty(value = "表单id")
    private String tableId;

    /**表单名称*/
    @Excel(name = "表单名称", width = 15)
    @ApiModelProperty(value = "表单名称")
    private String tableName;

    /**表单编码*/
    @Excel(name = "表单编码", width = 15)
    @ApiModelProperty(value = "表单编码")
    private String tableCode;

    /**流程状态列名*/
    @Excel(name = "流程状态列名", width = 15)
    @ApiModelProperty(value = "流程状态列名")
    private String  tableBpm;

    /**业务标题*/
    @Excel(name = "业务标题", width = 15)
    @ApiModelProperty(value = "业务标题")
    private String workTitle;

    @Override
    public String toString() {
        return "BpmToolTableBpmnInfoVO{" +
                "id='" + id + '\'' +
                ", bpmId='" + bpmId + '\'' +
                ", bpmKey='" + bpmKey + '\'' +
                ", tableCate='" + tableCate + '\'' +
                ", tableId='" + tableId + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", tableBpm='" + tableBpm + '\'' +
                ", workTitle='" + workTitle + '\'' +
                '}';
    }
}
