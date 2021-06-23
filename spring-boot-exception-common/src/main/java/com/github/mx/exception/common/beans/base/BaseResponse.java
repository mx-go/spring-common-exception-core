package com.github.mx.exception.common.beans.base;

import com.github.mx.exception.common.constant.CommonErrorCode;
import com.github.mx.exception.common.constant.IResponseEnum;
import lombok.Data;

/**
 * 基础返回结果
 * <p>
 * Create by max on 2021/06/22
 */
@Data
public class BaseResponse {
    /**
     * 返回码
     */
    protected int code;
    /**
     * 返回消息
     */
    protected String message;
    /**
     * 记录trace信息
     */
    protected String traceMsg;

    public BaseResponse() {
        this(CommonErrorCode.SUCCESS);
    }

    public BaseResponse(IResponseEnum responseEnum) {
        this(responseEnum.getCode(), responseEnum.getMessage());
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, String traceMsg) {
        this.code = code;
        this.message = message;
        this.traceMsg = traceMsg;
    }

    public boolean isSuccess() {
        return CommonErrorCode.SUCCESS.getCode() == this.code;
    }
}