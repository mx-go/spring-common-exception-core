package com.github.mx.exception.common.exception.assertion;

import com.github.mx.exception.common.constant.IResponseEnum;
import com.github.mx.exception.common.exception.ArgumentException;
import com.github.mx.exception.common.exception.BaseException;

import java.text.MessageFormat;

/**
 * 参数不合法
 * <p>
 * Create by max on 2021/06/22
 */
public interface ArgumentExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (args != null && args.length != 0) {
            msg = MessageFormat.format(this.getMessage(), args);
        }
        return new ArgumentException(this, args, msg, t);
    }
}
