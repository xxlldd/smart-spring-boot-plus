package com.github.dongglin.smart.spring.boot.plus.common.exception;

import com.github.dongglin.smart.spring.boot.plus.common.enums.StatusCode;

/**
 * 代码编码生成异常
 */
public class JavaCompilerException extends BaseException {
    private Integer errorCode;
    private String message;

    public JavaCompilerException(Integer errorCode, String message) {
        super(errorCode,message);
        this.message = message;
        this.errorCode = errorCode;
    }
    public JavaCompilerException() {
        super(StatusCode.CODE_600001);
        this.message = StatusCode.CODE_600001.getMessage();
        this.errorCode = StatusCode.CODE_600001.getCode();
    }

    public JavaCompilerException(String message) {
        super(StatusCode.CODE_600001.getCode(),message);
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
