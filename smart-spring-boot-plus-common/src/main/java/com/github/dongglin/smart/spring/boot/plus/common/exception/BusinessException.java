package com.github.dongglin.smart.spring.boot.plus.common.exception;

import com.github.dongglin.smart.spring.boot.plus.common.enums.IResponseEnum;

/**
 * <p>业务异常</p>
 * <p>业务处理时，出现异常，可以抛出该异常</p>
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }
    public BusinessException(IResponseEnum responseEnum, String message) {
        super(responseEnum, null, message);
    }

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
