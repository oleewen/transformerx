package com.transformer.page;

import lombok.Data;
import org.springframework.util.Assert;

@Data
public class Pager implements Page {
    public static final int DEFAULT_PAGE_SIZE = 100;
    public static final int MAX_PAGE_SIZE = 500;

    /**
     * 当前页
     */
    private int pageNum = 1;

    /**
     * 页容量
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 总记录数
     */
    private int total;

    public void setPageNum(int pageNum) {
        Assert.isTrue(pageNum > 0, "pageNum should gt 0");

        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        Assert.isTrue(pageSize >= 0, "pageSize should ge 0");
        Assert.isTrue(pageSize <= MAX_PAGE_SIZE, "pageSize is too many");

        this.pageSize = pageSize;
    }

    public int getTotal() {
        return this.total;
    }

    public int setTotal(int total) {
        Assert.isTrue(total >= 0, "total should ge 0");

        return this.total = total;
    }

    public int getPageCount() {
        int total = getTotal();
        int pageSize = getPageSize();
        int pageCount = total / pageSize;
        if (total % pageSize != 0) {
            pageCount++;
        }
        return pageCount;
    }

    public int getFrom() {
        return (getPageNum() - 1) * getPageSize();
    }
}
