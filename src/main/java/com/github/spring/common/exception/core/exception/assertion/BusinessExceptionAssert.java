package com.github.spring.common.exception.core.exception.assertion;

import com.github.spring.common.exception.core.constant.IResponseEnum;
import com.github.spring.common.exception.core.exception.BaseException;
import com.github.spring.common.exception.core.exception.BusinessException;

import java.text.MessageFormat;

/**
 * 业务异常断言
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg, t);
    }

}
