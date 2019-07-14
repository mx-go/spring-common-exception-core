package com.github.spring.common.exception.core.pojo.param;

import com.github.spring.common.exception.core.pojo.request.BaseRequest;

/**
 * <p>查询条件参数类</p>
 */
public class QueryParam extends BaseRequest {
    /**
     * 默认页码，第一页
     */
    private static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认分页大小，默认20条记录
     */
    private static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 页码
     */
    protected Integer pageNum = DEFAULT_PAGE_NUM;
    /**
     * 分页大小
     */
    protected Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * @return 页码
     */
    public Integer getPageNum() {
        if (this.pageNum == null) {
            return DEFAULT_PAGE_NUM;
        }

        return this.pageNum;
    }

    /**
     * @param pageNum 页码
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * @return 分页大小
     */
    public Integer getPageSize() {
        if (this.pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    /**
     * @param pageSize 分页大小
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
