package com.transformer.exception;

import com.transformer.consts.StringConst;
import com.transformer.status.Status;
import com.transformer.status.StatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

/**
 * 内部运行异常，不可对外抛出
 *
 * @author only
 * @date 2017/7/10.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NestedRuntimeException extends RuntimeException implements Status {
    static final String SPLIT = StringConst.IMARK;
    private final int status;
    private String statusCode;
    private String message;

    public NestedRuntimeException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.status = StatusCode.INTERNAL_SERVER_ERROR.getStatus();
        this.statusCode = StatusCode.INTERNAL_SERVER_ERROR.getStatusCode();
        this.message = throwable.getMessage();
    }

    public NestedRuntimeException(Status status) {
        this(status.getStatus(), status.getStatusCode(), status.getMessage());
    }

    public NestedRuntimeException(Status status, Throwable throwable) {
        this(status.getStatus(), status.getStatusCode(), status.getMessage(), throwable);
    }

    public NestedRuntimeException(Status status, String message, Throwable throwable) {
        this(status.getStatus(), status.getStatusCode(), message, throwable);
    }

    public NestedRuntimeException(int status, String errorCode, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.statusCode = errorCode;
        this.message = errorMessage;
    }

    public NestedRuntimeException(int status, String errorCode, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.status = status;
        this.statusCode = errorCode;
        this.message = errorMessage;
    }

    public static Status parseMessage(final String message) {
        String[] parts = Optional.ofNullable(message)
                .filter(msg -> msg.contains(SPLIT))
                .map(msg -> msg.split(SPLIT))
                .orElse(new String[]{"", "", ""});

        int status = parts.length > 0 && NumberUtils.isDigits(parts[0]) ? Integer.parseInt(parts[0]) : DEFAULT_STATUS;
        String errorCode = parts.length > 1 ? parts[1] : "";
        String errorMessage = parts.length > 2 ? parts[2] : "";

        return new Status() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public int getStatus() {
                return status;
            }

            @Override
            public String getStatusCode() {
                return errorCode;
            }

            @Override
            public String getMessage() {
                return errorMessage;
            }

            @Override
            public String getMessage(Object... format) {
                return errorMessage;
            }
        };
    }

    public String getFormattedMessage() {
        return String.format("%s%s%s%s%s", getStatus(), SPLIT, getStatusCode(), SPLIT, getMessage());
    }

    @Override
    public boolean isSuccess() {
        return status < StatusCode.BAD_REQUEST.getStatus();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getMessage(Object... format) {
        if (StringUtils.isNotBlank(getMessage())) {
            return String.format(getMessage(), format);
        }
        return StringConst.EMPTY;
    }
}
