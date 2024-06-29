package org.jeecg.modules.estar.bs.constant;

/**
 * 查询条件
 * @author nbacheng
 * @since 2023-03-16
 */
public enum QueryEnum {
    /**
     * 查询条件为相等
     */
    EQ,

    /**
     * 查询条件为like
     */
    LIKE,

    /**
     * 查询条件大于
     */
    GT,

    /**
     * 查询条件大于等于
     */
    GE,

    /**
     * 查询条件小于
     */
    LT,

    /**
     * 查询条件小于等于
     */
    LE,

    /**
     * 数据库IN
     */
    IN,

    /**
     * 数据库 not IN
     */
    NOT_IN,

    /**
     * 时间范围查询
     */
    BWT,

    /***
     * is null
     */
    IS_NULL,

    /***
     * not null
     */
    NOT_NULL,
}
