package com.github.dongglin.smart.spring.boot.plus.core.framework.exception;

/**
 * 业务异常
 *
 * @author DongGL
 * @date 2018-11-08
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

}
