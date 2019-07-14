package com.github.spring.common.exception.core.pojo.param;

import lombok.Data;

/**
 * 分页对象
 */
@Data
public class QueryPage<T> {

    /**
     * 页码
     */
    private Long pageNum = 1L;
    /**
     * 每页数量
     */
    private Long pageSize = 20L;
}