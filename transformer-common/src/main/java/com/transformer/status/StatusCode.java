package com.transformer.status;

import java.text.MessageFormat;

/**
 * 通用状态码
 */
public enum StatusCode implements Status {
    UNKNOWN_ERROR(DEFAULT_STATUS, "未知异常"),

    // 1xx 信息性状态码
    CONTINUE(100, "Continue"), // 请继续
    SWITCHING_PROTOCOLS(101, "Switching Protocols"), // 为响应请求头Upgrade，切换协议
    PROCESSING(102, "Processing"), // 处理中
    EARLY_HINTS(103, "Early Hints"), // 提前提示

    // 2xx 成功状态码
    OK(200, "OK"), // 请求成功
    CREATED(201, "Created"), // 请求成功并创建了新资源（通常用于POST或PUT请求）
    ACCEPTED(202, "Accepted"), // 请求已接受但尚未响应，未有处理结果
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"), // 请求已处理成功，但返回的Meta信息不在原始服务器上，而是一个副本
    NO_CONTENT(204, "No Content"), // 请求已处理成功，但无响应内容
    RESET_CONTENT(205, "Reset Content"), // 请求已处理成功，可重置表单内容
    PARTIAL_CONTENT(206, "Partial Content"), // 成功处理了部分GET内容
    MULTI_STATUS(207, "Multi-Status"), // 代表之后的消息体将是一个XML消息，并且可能依照之前子请求数量的不同，包含一系列独立的响应代码
    ALREADY_REPORTED(208, "Already Reported"), // DAV绑定的成员已经在（多状态）响应之前的部分被列举，且未被再次包含
    IM_USED(226, "IM Used"), // 服务器已经满足了对资源的请求，对实体请求的一个或多个实体操作的结果表示

    // 3xx 重定向状态码
    MULTIPLE_CHOICES(300, "Multiple Choices"), // 多种选择：被请求的资源有一系列可供选择的回馈信息
    MOVED_PERMANENTLY(301, "Moved Permanently"), // 永久移动：被请求的资源已永久移动到新位置
    FOUND(302, "Found"), // 临时重定向：要求客户端执行临时重定向（原始描述短语为“Moved Temporarily”）
    SEE_OTHER(303, "See Other"), // 对应当前请求的响应可以在另一个URI上被找到
    NOT_MODIFIED(304, "Not Modified"), // 未修改：表示资源在由请求头中的If-Modified-Since或If-None-Match参数指定的这一版本之后，未曾被修改
    USE_PROXY(305, "Use Proxy"), // 使用代理：被请求的资源必须通过指定的代理才能被访问
    TEMPORARY_REDIRECT(307, "Temporary Redirect"), // 临时重定向：请求应该与另一个URI重复，但后续的请求应仍使用原始的URI
    PERMANENT_REDIRECT(308, "Permanent Redirect"), // 永久重定向：请求和所有将来的请求应该使用另一个URI重复

    // 4xx 客户端错误状态码
    BAD_REQUEST(400, "Bad Request"), // 错误请求：参数错误、请求语法错误、无效的请求消息或欺骗性路由请求 @see IllegalArgumentException
    UNAUTHORIZED(401, "Unauthorized"), // 未授权：当前请求需要用户验证（登录、权限） @see IllegalAccessException
    PAYMENT_REQUIRED(402, "Payment Required"), // 需要付款：该状态码是为了与401状态码区分，表示用户已被授权，但需要付款
    FORBIDDEN(403, "Forbidden"), // 服务器拒绝执行请求 @see RejectedExecutionException
    NOT_FOUND(404, "Not Found"), // 未找到请求的资源 @see ObjectNotFoundException
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"), // 方法不被允许 @see NoSuchMethodException
    NOT_ACCEPTABLE(406, "Not Acceptable"), // 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体，该请求不可接受
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"), // 类似401，需要代理服务器身份验证 @see IllegalAccessException
    REQUEST_TIMEOUT(408, "Request Timeout"), // 请求超时 @see TimeoutException
    CONFLICT(409, "Conflict"), // 冲突：表示因为请求存在冲突无法处理该请求
    GONE(410, "Gone"), // 资源已消失：表示所请求的资源不再可用，将不再可用
    LENGTH_REQUIRED(411, "Length Required"), // 需要内容长度：服务器拒绝在没有定义Content-Length头的情况下接受请求
    PRECONDITION_FAILED(412, "Precondition Failed"), // 前提条件失败：服务器在验证在请求的头字段中给出先决条件时，没能满足其中的一个或多个
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"), // 负载过大：请求提交的实体数据大小超过了服务器愿意或者能够处理的范围 @see ContentTooLongException
    URI_TOO_LONG(414, "URI Too Long"), // URI过长
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"), // 不支持的媒体类型：图片、视频格式
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"), // 范围不可满足
    EXPECTATION_FAILED(417, "Expectation Failed"), // 期望失败
    MISDIRECTED_REQUEST(421, "Misdirected Request"), // 错误请求
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"), // 无法处理的实体
    LOCKED(423, "Locked"), // 锁定：当前资源被锁定
    FAILED_DEPENDENCY(424, "Failed Dependency"), // 依赖失败
    TOO_EARLY(425, "Too Early"), // 太早
    UPGRADE_REQUIRED(426, "Upgrade Required"), // 需要升级
    PRECONDITION_REQUIRED(428, "Precondition Required"), // 需要前提条件
    TOO_MANY_REQUESTS(429, "Too Many Requests"), // 请求过多 @see FlowException
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"), // 请求头字段过大
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"), // 出于法律原因不可用

    // 5xx 服务器错误状态码
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"), // 服务器内部错误 @see RuntimeException BusinessException
    NOT_IMPLEMENTED(501, "Not Implemented"), // 未实现 @see UnsupportedOperationException
    BAD_GATEWAY(502, "Bad Gateway"), // 错误网关 @see CallServiceException
    SERVICE_UNAVAILABLE(503, "Service Unavailable"), // 服务不可用 @see ServiceUnavailableException
    GATEWAY_TIMEOUT(504, "Gateway Timeout"), // 网关超时 @see TimeoutException
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"), // 不支持的HTTP版本 @see HttpVersionNotSupportedException
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"), // 变体也进行协商
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"), // 存储不足 @see PersistenceException
    LOOP_DETECTED(508, "Loop Detected"), // 检测到循环 @see StackOverflowError
    NOT_EXTENDED(510, "Not Extended"), // 不扩展
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"); // 需要网络身份验证 @see IllegalAccessException

    private final int status;
    private final String message;

    StatusCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static StatusCode instance(int status) {
        for (StatusCode each : StatusCode.values()) {
            if (status == each.status) {
                return each;
            }
        }
        return UNKNOWN_ERROR;
    }

    @Override
    public boolean isSuccess() {
        return this.getStatus() < BAD_REQUEST.getStatus();
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusCode() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(Object... format) {
        return MessageFormat.format(message, format);
    }

    public Status with(String message) {
        assert message != null;

        return new Status() {
            @Override
            public boolean isSuccess() {
                return StatusCode.this.isSuccess();
            }

            @Override
            public int getStatus() {
                return StatusCode.this.getStatus();
            }

            @Override
            public String getStatusCode() {
                return StatusCode.this.getStatusCode();
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public String getMessage(Object... format) {
                return MessageFormat.format(message, format);
            }
        };
    }
}