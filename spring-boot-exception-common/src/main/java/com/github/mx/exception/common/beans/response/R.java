package com.github.mx.exception.common.beans.response;

import com.github.mx.exception.common.beans.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 通用返回结果
 * <p>
 * Create by max on 2021/06/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class R<T> extends BaseResponse implements Serializable {

    private static final long serialVersionUID = -6975092986259063659L;
    /**
     * 数据列表
     */
    protected T data;

    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(int code, String msg) {
        super(code, msg);
    }

    public R(int code, String msg, String traceMsg) {
        super(code, msg, traceMsg);
    }
}