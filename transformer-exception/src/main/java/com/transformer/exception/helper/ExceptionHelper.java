package com.transformer.exception.helper;

import com.transformer.exception.NestedBusinessException;
import com.transformer.exception.ThrowableRuntimeException;
import com.transformer.status.Status;

/**
 * Created by only on 2017/3/13.
 */
public class ExceptionHelper {
    private ExceptionHelper(){}

    public static ThrowableRuntimeException createThrowableRuntimeException(String errorMessage) {
        return createThrowableRuntimeException(NestedBusinessException.parseMessage(errorMessage));
    }

    public static ThrowableRuntimeException createThrowableRuntimeException(Throwable t) {
        return new ThrowableRuntimeException(t);
    }

    public static ThrowableRuntimeException createThrowableRuntimeException(Status status) {
        return new ThrowableRuntimeException(status);
    }

    public static ThrowableRuntimeException createThrowableRuntimeException(Status status, Throwable t) {
        return new ThrowableRuntimeException(status, t);
    }

    public static ThrowableRuntimeException createThrowableRuntimeException(String errorCode, String message) {
        return createThrowableRuntimeException(Status.DEFAULT_STATUS, errorCode, message);
    }

    public static ThrowableRuntimeException createThrowableRuntimeException(int status, String errorCode, String message) {
        return new ThrowableRuntimeException(status, errorCode, message);
    }

    public static ThrowableRuntimeException createThrowableRuntimeException(int status, String errorCode, String message, Throwable t) {
        return new ThrowableRuntimeException(status, errorCode, message, t);
    }

    public static NestedBusinessException createNestedException(String errorMessage) {
        return createNestedException(NestedBusinessException.parseMessage(errorMessage));
    }

    public static NestedBusinessException createNestedException(Throwable t) {
        return new NestedBusinessException(t);
    }

    public static NestedBusinessException createNestedException(Status status) {
        return new NestedBusinessException(status);
    }

    public static NestedBusinessException createNestedException(Status status, Throwable t) {
        return new NestedBusinessException(status, t);
    }

    public static NestedBusinessException createNestedException(String errorCode, String message) {
        return createNestedException(Status.DEFAULT_STATUS, errorCode, message);
    }

    public static NestedBusinessException createNestedException(int status, String errorCode, String message) {
        return new NestedBusinessException(status, errorCode, message);
    }

    public static NestedBusinessException createNestedException(int status, String errorCode, String message, Throwable t) {
        return new NestedBusinessException(status, errorCode, message, t);
    }
}
