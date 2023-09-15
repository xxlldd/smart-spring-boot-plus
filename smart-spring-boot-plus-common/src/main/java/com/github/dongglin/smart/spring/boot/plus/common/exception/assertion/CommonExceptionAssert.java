package com.github.dongglin.smart.spring.boot.plus.common.exception.assertion;

import cn.hutool.core.util.ArrayUtil;
import com.github.dongglin.smart.spring.boot.plus.common.enums.IResponseEnum;
import com.github.dongglin.smart.spring.boot.plus.common.exception.ArgumentException;
import com.github.dongglin.smart.spring.boot.plus.common.exception.BaseException;

import java.text.MessageFormat;

/**
 */
public interface CommonExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }
        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }
        return new ArgumentException(this, args, msg, t);
    }

}
