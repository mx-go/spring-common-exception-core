package com.github.mx.exception.common.beans.request;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.ToString;

/**
 * MP分页对象
 * <p>
 * Create by max on 2021/06/22
 */
@ToString(callSuper = true)
public class PageQuery<T> extends Page<T> {

    private static final long serialVersionUID = 6912414604580544829L;
    /**
     * 默认页数
     */
    private static final Long DEFAULT_PAGE_NO = 1L;
    /**
     * 默认每页大小
     */
    private static final Long DEFAULT_PAGE_SIZE = 10L;

    public PageQuery() {
        super(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    }

    public PageQuery(BasePageArg basePageArg) {
        super(basePageArg.getPageNum(), basePageArg.getPageSize());
    }

    public Long getPageNum() {
        return this.getCurrent();
    }

    public void setPageNum(String pageNum) {
        if (StringUtils.isNotBlank(pageNum)) {
            this.setCurrent(Integer.parseInt(pageNum));
        }
    }

    public Long getPageSize() {
        return this.getSize();
    }

    public void setPageSize(String pageSize) {
        if (StringUtils.isNotBlank(pageSize)) {
            this.setSize(Integer.parseInt(pageSize));
        }
    }
}