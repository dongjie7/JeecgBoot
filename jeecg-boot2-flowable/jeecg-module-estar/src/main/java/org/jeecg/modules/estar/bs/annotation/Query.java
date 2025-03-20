package org.jeecg.modules.estar.bs.annotation;

import org.jeecg.modules.estar.bs.constant.QueryEnum;
import java.lang.annotation.*;

/**
 * 查询条件注解
 * @author nbacheng
 * @since 2023-03-16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Query {
    /**
     * 查询条件，默认相等
     * @return
     */
    QueryEnum value() default QueryEnum.EQ;

    /**
     * 是否参与where条件，默认是true
     * @return
     */
    boolean where() default true;

    /**
     * 查询字段，当前字段的值取查表中哪个字段
     * @return
     */
    String column() default "";
}
