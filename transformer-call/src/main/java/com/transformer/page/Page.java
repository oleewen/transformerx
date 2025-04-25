package com.transformer.page;

import java.io.Serializable;

/**
 * @author ouliyuan 2023/9/14
 */
public interface Page extends Serializable {
    int getPageNum();

    int getPageSize();

    void setPageNum(int pageNum);

    void setPageSize(int pageSize);
}
