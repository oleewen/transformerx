package com.transformer.response;

import java.io.Serializable;

/**
 * @author ouliyuan 2023/9/1
 */
public abstract class Response<T> implements Serializable {
    /**
     * 将原生结果构建为响应结果
     *
     * @param response 原生结果
     * @return 查询响应结果
     */
    public abstract Response<T> build(T response);
}
