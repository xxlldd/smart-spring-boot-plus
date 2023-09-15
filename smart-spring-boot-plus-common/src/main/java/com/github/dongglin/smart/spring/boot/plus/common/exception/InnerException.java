package com.github.dongglin.smart.spring.boot.plus.common.exception;


import com.github.dongglin.smart.spring.boot.plus.common.enums.IResponseEnum;

public class InnerException extends BaseException {

    public InnerException(String msg) {
        super(msg);
    }

    public InnerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public InnerException(IResponseEnum businessCode) {
        super(businessCode);
    }

    public InnerException(IResponseEnum businessCode, Object data) {
        super(businessCode);
    }
}
