package com.github.mx.exception.common.exception;

import com.github.mx.exception.common.constant.IResponseEnum;

/**
 * 校验异常
 * 调用接口时，参数格式不合法可以抛出该异常
 */
public class ValidationException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ValidationException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public ValidationException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}