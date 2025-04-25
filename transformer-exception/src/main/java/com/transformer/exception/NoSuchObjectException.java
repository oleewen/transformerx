package com.transformer.exception;

import com.transformer.status.Status;
import com.transformer.status.StatusCode;

/**
 * 当请求的对象不存在时抛出此异常
 * 
 * @author ouliyuan 2023/6/30
 */
public class NoSuchObjectException extends ThrowableRuntimeException {
    
    public NoSuchObjectException(String message) {
        super(StatusCode.NOT_FOUND.getStatus(), StatusCode.NOT_FOUND.getStatusCode(), message);
    }

    public NoSuchObjectException(String message, Throwable throwable) {
        super(StatusCode.NOT_FOUND.getStatus(), StatusCode.NOT_FOUND.getStatusCode(), message, throwable);
    }

    public NoSuchObjectException(Status status) {
        super(status);
    }

    public NoSuchObjectException(Status status, Throwable throwable) {
        super(status, throwable);
    }

    public NoSuchObjectException(Throwable throwable) {
        super(throwable);
    }
} 