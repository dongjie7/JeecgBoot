package org.jeecg.modules.estar.bs.annotation;

import java.lang.annotation.*;

import org.jeecg.modules.estar.bs.enums.FormatterEnum;

/**
 * 字段
 * @author nbacheng
 * @since 2023-03-23
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormatterType {

    FormatterEnum type() default FormatterEnum.OBJECT;

    Class target() default Object.class;
}
