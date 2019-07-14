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
    }

    public QueryDataResponse(QueryData<T> data) {
        super(data);
    }
}
