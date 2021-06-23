package com.github.mx.exception.common.beans.response;

import com.github.mx.exception.common.beans.base.Pager;
import com.github.mx.exception.common.constant.IResponseEnum;
import lombok.ToString;

/**
 * 分页响应信息
 * <p>
 * Create by max on 2021/06/22
 */
@ToString(callSuper = true)
public class PR<T> extends R<Pager<T>> {

    private static final long serialVersionUID = 922746983081258472L;

    public PR() {
        super();
    }

    public PR(Pager<T> data) {
        super(data);
    }

    public PR(int code, String msg) {
        super(code, msg);
    }

    public PR(int code, String msg, String traceMsg) {
        super(code, msg, traceMsg);
    }

    public static <T> PR<T> fail(IResponseEnum responseEnum) {
        return new PR<>(responseEnum.getCode(), responseEnum.getMessage());
    }
}