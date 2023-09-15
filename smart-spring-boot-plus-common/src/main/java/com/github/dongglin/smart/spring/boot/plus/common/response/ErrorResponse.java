package com.github.dongglin.smart.spring.boot.plus.common.response;

/**
 * <p>错误返回结果</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class ErrorResponse extends ApiResponse {

    private ErrorResponse() {
    }

    public ErrorResponse(int code, String message) {
        super(code, message);
    }
}
