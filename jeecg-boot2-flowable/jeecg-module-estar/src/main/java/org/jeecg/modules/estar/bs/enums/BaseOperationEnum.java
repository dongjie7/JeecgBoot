package org.jeecg.modules.estar.bs.enums;

/**
 * 操作枚举
 * @author nbacheng
 * @since 2023-03-23
 */
public enum BaseOperationEnum {

    /**
     * 插入操作枚举
     */
    INSERT,
    /**
     * 更新操作枚举
     */
    UPDATE,
    /**
     * 删除操作枚举
     */
    DELETE,
    /**
     * 批量删除操作枚举
     */
    DELETE_BATCH,

    /***
     * 先删除再添加
     */
    DELETE_INSERT,
    /***
     * 合并
     */
    MERGE
}
