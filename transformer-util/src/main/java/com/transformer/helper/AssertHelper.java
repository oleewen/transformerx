package com.transformer.helper;

import com.transformer.exception.helper.ExceptionHelper;
import com.transformer.status.StatusCode;
import com.transformer.status.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 业务断言工具类
 */
public abstract class AssertHelper {

    public static void isNull(@Nullable final Object object, Status status, String message) {
        if (object != null) {
            throwAssertException(status, message);
        }
    }

    public static void isNull(@Nullable final Object object, Status status, Object... format) {
        isNull(object, status, status.getMessage(format));
    }

    public static void notNull(@Nullable final Object object, Status status, String message) {
        if (object == null) {
            throwAssertException(status, message);
        }
    }

    public static void notNull(@Nullable final Object object, Status status, Object... format) {
        notNull(object, status, status.getMessage(format));
    }

    public static void isTrue(final boolean expression, Status status, String message) {
        if (!expression) {
            throwAssertException(status, message);
        }
    }

    public static void isTrue(final boolean expression, Status status, Object... format) {
        isTrue(expression, status, status.getMessage(format));
    }

    public static void isFalse(final boolean expression, Status status, String message) {
        if (expression) {
            throwAssertException(status, message);
        }
    }

    public static void isFalse(final boolean expression, Status status, Object... format) {
        isFalse(expression, status, status.getMessage(format));
    }

    @SuppressWarnings("rawtypes")
    public static void isEmpty(@Nullable final Collection object, Status status, String message) {
        if (object != null && !object.isEmpty()) {
            throwAssertException(status, message);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void isEmpty(@Nullable final Collection object, Status status, Object... format) {
        isEmpty(object, status, status.getMessage(format));
    }

    @SuppressWarnings("rawtypes")
    public static void isNotEmpty(@Nullable final Collection object, Status status, String message) {
        if (object == null || object.isEmpty()) {
            throwAssertException(status, message);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void isNotEmpty(@Nullable final Collection object, Status status, Object... format) {
        isNotEmpty(object, status, status.getMessage(format));
    }

    public static void isNotBlank(@Nullable final String string, Status status, String message) {
        if (StringUtils.isBlank(string)) {
            throwAssertException(status, message);
        }
    }

    public static void isNotBlank(@Nullable final String string, Status status, Object... format) {
        isNotBlank(string, status, status.getMessage(format));
    }

    public static void isBlank(@Nullable final String string, Status status, String message) {
        if (StringUtils.isNotBlank(string)) {
            throwAssertException(status, message);
        }
    }

    public static void isBlank(String string, Status status, Object... format) {
        isBlank(string, status, status.getMessage(format));
    }

    public static void matches(@Nullable final String string, Pattern pattern, Status status, String message) {
        if (!pattern.matcher(string).matches()) {
            throwAssertException(status, message);
        }
    }

    public static void matches(@Nullable final String string, Pattern pattern, Status status, Object... format) {
        matches(string, pattern, status, status.getMessage(format));
    }

    private static void throwAssertException(Status status, String message) {
        throw ExceptionHelper.createNestedException(status.getStatus(), status.getStatusCode(), message);
    }

    public static void isNull(@Nullable final Object object, String statusCode, String message) {
        if (object != null) {
            throwAssertException(statusCode, message);
        }
    }

    public static void notNull(@Nullable final Object object, String statusCode, String message) {
        if (object == null) {
            throwAssertException(statusCode, message);
        }
    }

    public static void isTrue(final boolean expression, String statusCode, String message) {
        if (!expression) {
            throwAssertException(statusCode, message);
        }
    }

    public static void isFalse(final boolean expression, String statusCode, String message) {
        if (expression) {
            throwAssertException(statusCode, message);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void isEmpty(@Nullable final Collection object, String statusCode, String message) {
        if (object != null && !object.isEmpty()) {
            throwAssertException(statusCode, message);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void isNotEmpty(@Nullable final Collection object, String statusCode, String message) {
        if (object == null || object.isEmpty()) {
            throwAssertException(statusCode, message);
        }
    }

    public static void isNotBlank(@Nullable final String string, String statusCode, String message) {
        if (StringUtils.isBlank(string)) {
            throwAssertException(statusCode, message);
        }
    }

    public static void isBlank(@Nullable final String string, String statusCode, String message) {
        if (StringUtils.isNotBlank(string)) {
            throwAssertException(statusCode, message);
        }
    }

    public static void matches(@Nullable final String string, Pattern pattern, String statusCode, String message) {
        if (!pattern.matcher(string).matches()) {
            throwAssertException(statusCode, message);
        }
    }

    private static void throwAssertException(String statusCode, String message) {
        Status status = Arrays.stream(StatusCode.values()).filter(e -> e.getStatusCode().equals(statusCode)).findFirst().orElse(StatusCode.UNKNOWN_ERROR);
        if (status != StatusCode.UNKNOWN_ERROR) {
            throwAssertException(status, message);
        } else {
            throw ExceptionHelper.createNestedException(statusCode, message);
        }
    }

}