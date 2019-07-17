package com.github.spring.common.exception.core.pojo.response;

import lombok.ToString;

/**
 * 响应信息主体，同{@link QueryDataResponse}
 *
 * @see QueryDataResponse
 */
@ToString
public class QR<T> extends QueryDataResponse<T> {

    public QR() {
        super();
    }

    public QR(int code, String msg) {
        super(code, msg);
    }

    public QR(QueryData<T> data) {
        super(data);
    }
}
