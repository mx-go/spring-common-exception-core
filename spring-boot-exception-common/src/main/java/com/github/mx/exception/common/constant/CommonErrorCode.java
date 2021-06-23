package com.github.mx.exception.common.constant;

import com.github.mx.exception.common.beans.base.BaseResponse;
import com.github.mx.exception.common.exception.BaseException;
import com.github.mx.exception.common.exception.assertion.CommonExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用返回结果
 * <p>
 * Create by max on 2021/06/22
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements CommonExceptionAssert {

    /**
     * 200表示成功
     */
    SUCCESS(200, "成功"),
    /**
     * 服务器繁忙，请稍后重试
     */
    SERVER_BUSY(9998, "服务器繁忙"),
    /**
     * 服务器异常，无法识别的异常，尽可能对通过判断减少未定义异常抛出
     */
    SERVER_ERROR(9999, "网络异常"),

    /**
     * 5开头错误码，一般对应于{@link ArithmeticException}，系统封装的工具出现异常
     */
    DATE_NOT_NULL(5001, "日期不能为空"),
    DATETIME_NOT_NULL(5001, "时间不能为空"),
    TIME_NOT_NULL(5001, "时间不能为空"),
    DATE_PATTERN_MISMATCH(5002, "日期{0}与格式{1}不匹配，无法解析"),
    PATTERN_NOT_NULL(5003, "日期格式不能为空"),
    PATTERN_INVALID(5003, "日期格式{0}无法识别"),

    /**
     * 绑定参数校验异常
     */
    VALID_ERROR(6000, "参数校验异常"),
    ;

    /**
     * 返回码
     */
    private final int code;
    /**
     * 返回消息
     */
    private final String message;

    /**
     * 校验返回结果是否成功
     *
     * @param response 远程调用的响应
     */
    public static void assertSuccess(BaseResponse response) {
        SERVER_ERROR.assertNotNull(response);
        int code = response.getCode();
        if (CommonErrorCode.SUCCESS.getCode() != code) {
            String msg = response.getMessage();
            String traceMsg = response.getTraceMsg();
            throw new BaseException(code, msg, traceMsg);
        }
    }
}
