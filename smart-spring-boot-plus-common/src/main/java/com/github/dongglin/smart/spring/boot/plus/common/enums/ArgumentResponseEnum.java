package com.github.dongglin.smart.spring.boot.plus.common.enums;

import com.github.dongglin.smart.spring.boot.plus.common.exception.assertion.CommonExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>参数校验异常返回结果</p>
 */
@Getter
@AllArgsConstructor
public enum ArgumentResponseEnum implements CommonExceptionAssert {
    /**
     * 绑定参数校验异常
     */
    VALID_ERROR(300004, "参数校验异常"),

    ;

    /**
     * 返回码
     */
    private final int code;
    /**
     * 返回消息
     */
    private final String message;

}
