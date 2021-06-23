package com.github.mx.exception.common.util;

import com.github.mx.exception.common.beans.base.Pager;
import com.github.mx.exception.common.beans.response.PR;
import com.github.mx.exception.common.beans.response.R;
import com.github.mx.exception.common.constant.CommonErrorCode;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * 远程调用工具类
 * <p>
 * Create by max on 2021/06/22
 */
@UtilityClass
public class ClientUtils {

    /**
     * 封装远程调用, 只返回关心的内容
     *
     * @param <T>      关心的内容类型
     * @param supplier 远程调用真正逻辑,
     * @return 关心的内容
     */
    public <T> T execute(Supplier<R<T>> supplier) {
        R<T> r = supplier.get();
        CommonErrorCode.assertSuccess(r);
        return r.getData();
    }

    /**
     * 封装分页远程调用, 只返回关心的内容
     *
     * @param <T>      关心的内容类型
     * @param supplier 程调用真正逻辑
     * @return 关心的内容
     */
    public <T> Pager<T> executePage(Supplier<PR<T>> supplier) {
        PR<T> pr = supplier.get();
        CommonErrorCode.assertSuccess(pr);
        return pr.getData();
    }
}
