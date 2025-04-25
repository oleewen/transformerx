package com.transformer.es.query;

import java.io.Serializable;

/**
 * @author ouliyuan 2024/7/31
 */
public interface Query<T, R> extends Serializable {
    T getBuilder();
}
