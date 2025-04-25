package com.transformer.es.result;

import lombok.Data;

@Data
public abstract class AbstractResult<T, R> implements Result<T, R> {
    /**
     * 请求id
     */
    private String requestId;

    /**
     * 搜索结果
     */
    private R response;
}
