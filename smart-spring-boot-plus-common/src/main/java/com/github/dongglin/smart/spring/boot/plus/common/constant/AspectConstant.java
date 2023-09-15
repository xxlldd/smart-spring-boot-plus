package com.github.dongglin.smart.spring.boot.plus.common.constant;

/**
 * @author DongGL
 * @date 2022/7/3
 **/
public interface AspectConstant {

    String COMMON_POINTCUT = "execution(public * " + CommonConstant.PACKAGE_NAME + "..*.controller..*.*(..))";

}
