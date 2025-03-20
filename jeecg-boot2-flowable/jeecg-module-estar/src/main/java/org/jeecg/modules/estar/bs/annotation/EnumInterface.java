package org.jeecg.modules.estar.bs.annotation;

/**
 * @author nbacheng
 * @since 2023-03-23
 */
public interface EnumInterface<T> {

    /**
     * 判断是否存在
     * @param value
     * @return
     */
    Boolean exist(T value);
}
