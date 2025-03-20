package org.jeecg.modules.flowable.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: WangYuZhou
 * @create: 2022-09-02 16:30
 * @description: 数据库传参供流程消息提醒返回参会使用
 **/

@Data
public class AnnouncementVo implements Serializable {

    private String procInsId;

    private String taskId;

    private String procDefId;


    @ApiModelProperty(value = "部署id")
    private String deployId;

    @ApiModelProperty(value = "表单类型 1 在线设计  2 Online表单")
    private String tableCategory;

    @Override
    public String toString() {
        return "AnnouncementVo{" +
                "procInsId='" + procInsId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", procDefId='" + procDefId + '\'' +
                ", deployId='" + deployId + '\'' +
                ", tableCategory='" + tableCategory + '\'' +
                '}';
    }
}
