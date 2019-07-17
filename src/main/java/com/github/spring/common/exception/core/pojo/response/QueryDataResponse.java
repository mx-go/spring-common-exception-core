package com.github.spring.common.exception.core.pojo.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询数据集返回结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryDataResponse<T> extends CommonResponse<QueryData<T>> {

    public QueryDataResponse() {
        super();
    }

    public QueryDataResponse(int code, String msg) {
        super(code, msg);
    }

    public QueryDataResponse(QueryData<T> data) {
        super(data);
    }
}
