package com.transformer.response;

import com.transformer.consts.StringConst;
import com.transformer.exception.NestedRuntimeException;
import com.transformer.helper.JsonHelper;
import com.transformer.result.ResultSupport;
import com.transformer.status.Status;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Result<T> implements ResultSupport {
    /** Define long serialVersionUID */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -1L;
    /** 状态 */
    private boolean success = true;
    /** 状态码：参考通用的HTTP状态码 */
    private int status;
    /** 错误码：参考通用的HTTP状态描述 */
    private String statusCode;
    /** 状态消息 */
    private String message;
    /** 响应module */
    private T module;

    /** 构造函数 */
    public Result() {
        super();
    }

    public Result(Status status) {
        this(status.isSuccess(), status.getStatus(), status.getStatusCode(), status.getMessage());
    }

    @SuppressWarnings("rawtypes")
    public Result(boolean success, int status, String statusCode, String message) {
        Result result = new Result();

        result.setSuccess(success);
        result.setStatus(status);
        result.setStatusCode(statusCode);
        result.setMessage(message);
    }

    @SuppressWarnings("rawtypes")
    public Result(boolean success, int status, String statusCode, String message, String[] messages) {
        Result result = new Result();

        result.setSuccess(success);
        result.setStatus(status);
        result.setStatusCode(statusCode);
        if (message != null && message.contains("%s")) {
            if (messages != null) {
                message = String.format(message, (Object[]) messages);
            }
            // 无替换文本
            else {
                message = message.replaceAll("%s", "");
            }
        }
        result.setMessage(message);
    }

    /**
     * 设置状态码
     *
     * @param status 状态码
     */
    @SuppressWarnings("rawtypes")
    public static Result create(Status status) {
        return create(status.isSuccess(), status.getStatus(), status.getStatusCode(), status.getMessage());
    }

    /**
     * 根据异常设置状态码
     *
     * @param e 异常
     */
    @SuppressWarnings("rawtypes")
    public static Result create(NestedRuntimeException e) {
        return create(false, e.getStatus(), e.getStatusCode(), e.getMessage());
    }

    /**
     * 设置状态码
     *
     * @param status 状态码
     */
    @SuppressWarnings("rawtypes")
    public static Result create(boolean success, int status, String errorCode, String message) {
        return new Result(success, status, errorCode, message);
    }

    /**
     * 设置状态码
     *
     * @param status   状态码
     * @param messages 替换文本
     */
    @SuppressWarnings("rawtypes")
    public static Result create(Status status, String... messages) {
        return create(status.isSuccess(), status.getStatus(), status.getStatusCode(), status.getMessage(), messages);
    }

    /**
     * 设置状态码
     *
     * @param status   状态码
     * @param messages 替换文本
     */
    @SuppressWarnings("rawtypes")
    public static Result create(boolean success, int status, String errorCode, String message, String... messages) {
        return new Result(success, status, errorCode, message, messages);
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

    @Override
    public String toString() {
        return JsonHelper.toJson(this);
    }
}
