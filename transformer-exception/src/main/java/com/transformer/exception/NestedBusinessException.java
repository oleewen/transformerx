package com.transformer.exception;

import com.transformer.consts.StringConst;
import com.transformer.status.ResultCodeEnum;
import com.transformer.status.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

/**
 * @author only
 * @date 2017/7/10.
 */
public class NestedBusinessException extends RuntimeException {
    static final String SPLIT = StringConst.IMARK;
    private Status status;

    public NestedBusinessException(Throwable throwable) {
        super(throwable);
        this.status = ResultCodeEnum.INTERNAL_SERVER_ERROR;
    }

    public NestedBusinessException(Status status) {
        super(status.getMessage());
        this.status = status;
    }

    public NestedBusinessException(Status status, Throwable throwable) {
        super(status.getMessage(), throwable);
        this.status = status;
    }

    public NestedBusinessException(int status, String errorCode, String errorMessage) {
        super(errorMessage);
        this.status = getStatus(status, errorCode, errorMessage);
    }

    public NestedBusinessException(int status, String errorCode, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.status = getStatus(status, errorCode, errorMessage);
    }

    public static Status parseMessage(final String message) {
        ResultCodeEnum resultCode = ResultCodeEnum.INTERNAL_SERVER_ERROR;
        if (StringUtils.isBlank(message)) {
            return resultCode;
        }

        if (!message.contains(SPLIT)) {
            return getStatus(resultCode.getStatus(), resultCode.getStatusCode(), message);
        }

        String[] parts = Optional.ofNullable(message)
                .filter(msg -> msg.contains(SPLIT))
                .map(msg -> msg.split(SPLIT))
                .orElse(new String[]{"", "", ""});

        int status = parts.length > 0 && NumberUtils.isDigits(parts[0]) ? Integer.parseInt(parts[0]) : resultCode.getStatus();
        String errorCode = parts.length > 1 ? parts[1] : resultCode.getStatusCode();
        String errorMessage = parts.length > 2 ? parts[2] : resultCode.getMessage();

        return getStatus(status, errorCode, errorMessage);
    }

    private static Status getStatus(int status, String errorCode, String errorMessage) {
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
                return String.format(getMessage(), format);
            }
        };
    }

    public Status getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return String.format("%s%s%s%s%s", getStatus().getStatus(), SPLIT, getStatus().getStatusCode(), SPLIT, super.getMessage());
    }
}
