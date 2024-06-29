package org.jeecg.modules.estar.bs.annotation;

import java.lang.annotation.*;

/**
 * DTO跳过指定字段
 * @author nbacheng
 * @since 2023-03-23
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface DtoSkip {
}
