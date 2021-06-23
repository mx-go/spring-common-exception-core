package com.github.mx.exception.common.exception;


import com.github.mx.exception.common.constant.IResponseEnum;

/**
 * 业务异常。业务处理时，出现异常，可以抛出该异常
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}