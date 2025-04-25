package com.transformer.es.result;

/**
 * @author ouliyuan 2024/7/31
 */
public interface Result<T, R> {
    T to();

    void setResponse(R response);
}
