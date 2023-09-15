package com.github.dongglin.smart.spring.boot.plus.common.annotation;

import com.github.dongglin.smart.spring.boot.plus.common.enums.Comparison;
import com.github.dongglin.smart.spring.boot.plus.common.enums.Strategy;

import javax.lang.model.type.NullType;
import java.lang.annotation.*;

/**
 * @author 11960
 * @ClassName BindQuery
 * @description: 绑定管理器
 * @date 2023年07月18日
 * @version: 1.0
 */
@Target(ElementType.FIELD)
@Repeatable(BindQuery.List.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BindQuery {

    /**
     * 查询条件
     */
    Comparison comparison() default Comparison.EQ;

    /**
     * 数据库字段，默认为空，自动根据驼峰转下划线
     */
    String column() default "";

    /**
     * 绑定的Entity类
     */
    Class<?> entity() default NullType.class;

    /**
     * JOIN连接条件，支持动态的跨表JOIN查询
     */
    String condition() default "";

    /**
     * 忽略该字段
     */
    boolean ignore() default false;

    /**
     * 查询处理策略：默认忽略空字符串
     */
    Strategy strategy() default Strategy.IGNORE_EMPTY;

    /**
     * 在同一个字段上支持多个{@link BindQuery}，之间会用采用OR的方式连接
     *
     * @author wind
     * @since v2.4.0
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        BindQuery[] value();

    }

}
