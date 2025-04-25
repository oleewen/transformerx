package com.transformer.request;

import com.transformer.page.Page;
import com.transformer.page.Pager;
import org.springframework.util.Assert;

public abstract class PageRequest implements Request, Page {
    /**
     * 分页
     */
    private final Pager pager = new Pager();

    @Override
    public int getPageNum() {
        return pager.getPageNum();
    }

    @Override
    public int getPageSize() {
        return pager.getPageSize();
    }

    public int getFrom() {
        return pager.getFrom();
    }

    @Override
    public void setPageNum(int pageNum) {
        Assert.isTrue(pageNum >= 0, "pageNum must be greater than or equal to 0");
        pager.setPageNum(pageNum);
    }

    @Override
    public void setPageSize(int pageSize) {
        Assert.isTrue(pageSize >= 0, "pageSize must be greater than or equal to 0");
        pager.setPageSize(pageSize);
    }
}
