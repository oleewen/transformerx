package com.transformer.result;

import com.transformer.status.Status;

/**
 * @author ouliyuan 2024/1/19
 */
public interface ResultSupport<T> extends Status {
    T getModule();
}
