package com.github.dongglin.smart.spring.boot.plus.common.annotation;


import com.github.dongglin.smart.spring.boot.plus.common.enums.SysLogEnum;

/**
 * @author DongGL
 * @Description: 　日志
 * @date 2023/9/15 11:05
 */
public @interface Log {

    /**
     * 描述
     *
     * @return
     */
    String value() default "";


    SysLogEnum type() default SysLogEnum.OTHER;

}
