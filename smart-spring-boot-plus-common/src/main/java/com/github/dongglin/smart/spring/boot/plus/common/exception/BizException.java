package com.github.dongglin.smart.spring.boot.plus.common.exception;

import com.github.dongglin.smart.spring.boot.plus.common.enums.IResponseEnum;

public class BizException extends BaseException {
    private int code;
    protected Object msg;

    public BizException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BizException(int code, String msg) {
        super(code,msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public BizException(IResponseEnum businessCode) {
        super(businessCode);
        this.code = businessCode.getCode();
        this.msg = businessCode.getMessage();
    }

    public BizException(IResponseEnum businessCode, Object data) {
        super(businessCode);
        this.code = businessCode.getCode();
        this.msg = data;
    }

    public int getCode() {
        return this.code;
    }

    public Object getData() {
        return this.msg;
    }
}
