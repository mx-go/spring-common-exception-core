package com.github.spring.common.exception.core.exception.assertion;

import com.github.spring.common.exception.core.constant.IResponseEnum;
import com.github.spring.common.exception.core.exception.ArgumentException;
import com.github.spring.common.exception.core.exception.BaseException;

import java.text.MessageFormat;

/**
 * <pre>
 * 参数不合法
 * </pre>
 */
public interface ArgumentExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new ArgumentException(this, args, msg, t);
    }

}
