package com.transformer.response;

import com.transformer.page.Page;
import com.transformer.page.Pager;
import lombok.Data;
import org.springframework.util.Assert;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class PageResponse<T> extends ListResponse<T> implements Page {
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
        Assert.isTrue(pageNum > 0, "pageNum must be greater than or equal to 0");
        pager.setPageNum(pageNum);
    }

    @Override
    public void setPageSize(int pageSize) {
        Assert.isTrue(pageSize >= 0, "pageSize must be greater than or equal to 0");
        pager.setPageSize(pageSize);
    }

    public void setTotal(int total) {
        Assert.isTrue(total >= 0, "total must be greater than or equal to 0");
        super.setTotal(total);
        pager.setTotal(total);
    }
}
