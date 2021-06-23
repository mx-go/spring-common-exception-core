package com.github.mx.exception.common.beans.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页查询数据返回
 * <p>
 * Create by max on 2021/06/22
 */
@Data
public class Pager<T> {
    /**
     * 数据列表
     */
    private List<T> records;
    /**
     * 当前页，从1开始
     */
    private long pageNum;
    /**
     * 分页大小
     */
    private long pageSize;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 总页数
     */
    private long pages;

    public Pager() {
    }

    public Pager(IPage<T> page) {
        this.setRecords(page.getRecords());
        this.setPageNum(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotal(page.getTotal());
        this.setPages(page.getPages());
    }

    public <E> Pager(IPage<E> page, Function<E, T> mapper) {
        this.setPageNum(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotal(page.getTotal());
        this.setPages(page.getPages());

        if (CollectionUtils.isEmpty(page.getRecords())) {
            this.setRecords(Collections.emptyList());
        } else {
            this.setRecords(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        }
    }

    public Pager(List<T> records, long total, int pageNum, int pageSize, long pages) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
    }

    public <E> Pager(IPage<E> page, List<T> records) {
        this.records = records;
        this.setPageNum(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotal(page.getTotal());
        this.setPages(page.getPages());
    }
}
