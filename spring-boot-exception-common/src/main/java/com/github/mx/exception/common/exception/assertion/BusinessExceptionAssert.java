package com.github.mx.exception.common.exception.assertion;

import com.github.mx.exception.common.constant.IResponseEnum;
import com.github.mx.exception.common.exception.BaseException;
import com.github.mx.exception.common.exception.BusinessException;

import java.text.MessageFormat;

/**
 * 业务异常断言
 * <p>
 * Create by max on 2021/06/22
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getMessage();
        if (args != null && args.length != 0) {
            msg = MessageFormat.format(this.getMessage(), args);
        }
        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (args != null && args.length != 0) {
            msg = MessageFormat.format(this.getMessage(), args);
        }
        return new BusinessException(this, args, msg, t);
    }
}