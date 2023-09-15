package com.github.dongglin.smart.spring.boot.plus.common.exception;

/**
 * 代码生成异常
 */
public class ClazzGenerateException extends BaseException {
    private Integer errorCode;
    private String message;

    public ClazzGenerateException(Integer errorCode, String message) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ClazzGenerateException(String message) {
        super(message);
        this.message = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
