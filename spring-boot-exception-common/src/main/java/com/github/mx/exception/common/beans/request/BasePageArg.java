package com.github.mx.exception.common.beans.request;

import com.github.mx.exception.common.beans.base.BaseRequest;

/**
 * 分页查询条件参数类
 * <p>
 * Create by max on 2021/06/22
 */
public class BasePageArg extends BaseRequest {
    /**
     * 默认页码，第一页
     */
    private static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认分页大小，默认10条记录
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 页码
     */
    protected Integer pageNum = DEFAULT_PAGE_NUM;
    /**
     * 分页大小
     */
    protected Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 页码
     */
    public Integer getPageNum() {
        if (this.pageNum == null) {
            return DEFAULT_PAGE_NUM;
        }
        return this.pageNum;
    }

    /**
     * 页码
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 分页大小
     */
    public Integer getPageSize() {
        if (this.pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    /**
     * 分页大小
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}