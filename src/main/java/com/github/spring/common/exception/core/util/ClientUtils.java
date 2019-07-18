package com.github.spring.common.exception.core.util;

import com.github.spring.common.exception.core.constant.enums.CommonResponseEnum;
import com.github.spring.common.exception.core.pojo.response.QR;
import com.github.spring.common.exception.core.pojo.response.QueryData;
import com.github.spring.common.exception.core.pojo.response.R;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * 远程调用工具类
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
        CommonResponseEnum.assertSuccess(r);
        return r.getData();
    }

    /**
     * 封装远程调用, 只返回关心的内容
     *
     * @param <T>      关心的内容类型
     * @param supplier 程调用真正逻辑
     * @return 关心的内容
     */
    public <T> QueryData<T> executePage(Supplier<QR<T>> supplier) {
        QR<T> qr = supplier.get();
        CommonResponseEnum.assertSuccess(qr);
        return qr.getData();
    }
}
