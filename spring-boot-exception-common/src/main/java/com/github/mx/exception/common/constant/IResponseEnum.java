package com.github.mx.exception.common.constant;

/**
 * 异常返回码枚举接口
 * <p>
 * Create by max on 2021/06/22
 */
public interface IResponseEnum {
    /**
     * 获取返回码
     *
     * @return 返回码
     */
    int getCode();

    /**
     * 获取返回信息
     *
     * @return 返回信息
     */
    String getMessage();

    /**
     * 获取trace信息
     *
     * @return 返回信息
     */
    default String getTraceMessage() {
        return null;
    }
}