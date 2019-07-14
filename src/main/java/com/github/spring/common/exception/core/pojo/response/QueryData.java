package com.github.spring.common.exception.core.pojo.response;

import lombok.Data;

import java.util.List;

/**
 * 分页查询数据返回
 */
@Data
public class QueryData<T> {
    /**
     * 数据列表
     */
    private List<T> data;
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 当前页，从1开始
     */
    private long pageNum;
    /**
     * 分页大小
     */
    private long pageSize;

    public QueryData() {

    }

    public QueryData(List<T> data, long totalCount, int pageNum, int pageSize) {
        this.data = data;
        this.totalCount = totalCount;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
