package org.jeecg.modules.flowable.entity.vo;

import lombok.Data;

/**
 * @Description: 流程定义的用户任务列表
 * @Author: nbacheng
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Data
public class UserTaskVo {
	/**
     * 用户任务节点id
     */
    private String id;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 用户任务节点名称
     */
    private String name;

    /**
     * 办理人名称
     */
    private String assigneeName;
}
