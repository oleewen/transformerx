package com.transformer.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.google.common.base.Throwables;
import com.transformer.exception.NestedRuntimeException;
import com.transformer.exception.ThrowableRuntimeException;
import com.transformer.exception.helper.ExceptionHelper;
import com.transformer.helper.JsonHelper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Dubbo全局异常处理器：拦截有异常的Dubbo调用，记录异常，返回有错误状态码的Result
 * <ol>
 *  <li>不处理方法声明的异常</li>
 *  <li>仅处理非声明的内部异常且返回结果为titan.Result的方法</li>
 *  <li>包装错误的message为有错误状态码的返回结果</li>
 * </ol>
 *
 * @author ouliyuan 2023/6/30
 */
@Slf4j
@Activate(group = {Constants.PROVIDER}, order = 100)
public class GlobalExceptionFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();

        // 执行调用
        Result result = invoker.invoke(invocation);

        // 有异常
        if (result.hasException()) {
            Throwable t = result.getException();

            // 记录异常
            error(invoker, invocation, t, System.currentTimeMillis() - start);

            try {
                Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                // 方法已声明异常
                if (isMethodDeclaredException(t, method)) {
                    return result;
                }
                // 方法未声明异常 && 返回结果为titan.Result
                if (isResult(method)) {
                    // 未捕捉的异常（用内部异常进行封装）
                    if (!(t instanceof NestedRuntimeException)) {
                        t = ExceptionHelper.createThrowableException(t);
                    }
                    // 封装异常到titanResult
                    return onException((NestedRuntimeException) t);
                }
                return result;
            } catch (Exception e) {
                error(invoker, invocation, e, System.currentTimeMillis() - start);

                return withException(result, e);
            }
        }

        info(invoker, invocation, result, System.currentTimeMillis() - start);

        // 无异常
        return result;
    }

    private boolean isMethodDeclaredException(Throwable exception, Method method) {
        Class<?>[] declareExceptions = method.getExceptionTypes();
        // 查找方法声明异常
        for (Class<?> exceptionClass : declareExceptions) {
            if (exception.getClass().equals(exceptionClass)) {
                return true;
            }
        }
        return false;
    }

    private void error(Invoker<?> invoker, Invocation invocation, Throwable e, long cost) {
        String message = e.getMessage();
        if (e instanceof NestedRuntimeException) {
            message = ((NestedRuntimeException) e).getFormattedMessage();
        }
        log.error("exception when call:{}/{}/{}/{} exception:{}({}) cost:{}ms", RpcContext.getContext().getRemoteHost(), invoker.getInterface().getName(), invocation.getMethodName(),JsonHelper.getNonEmptyInstance().json(invocation.getArguments()), message, Throwables.getRootCause(e), cost);
    }

    private static void info(Invoker<?> invoker, Invocation invocation, Result result, long cost) {
        if (log.isInfoEnabled()) {
            log.info("success when call:{}/{}/{} arguments:{} result:{} cost:{}ms", RpcContext.getContext().getRemoteHost(), invoker.getInterface().getName(), invocation.getMethodName(), JsonHelper.getNonEmptyInstance().json(invocation.getArguments()), JsonHelper.getNonEmptyInstance().json(result.getValue()), cost);
        }
    }

    private boolean isResult(Method method) {
        return Objects.equals(method.getReturnType(), com.zto.titans.common.entity.Result.class);
    }

    private Result onException(NestedRuntimeException e) {
        // 封装有错误的Result
        com.zto.titans.common.entity.Result<Object> result = com.zto.titans.common.entity.Result.error(e.getStatusCode(), e.getMessage());

        // rpc Result携带原生result和异常
        return withException(result, e);
    }

    private Result withException(Object result, Throwable t) {
        RpcResult rpcResult;
        if (result instanceof RpcResult) {
            rpcResult = ((RpcResult) result);
        } else {
            rpcResult = new RpcResult(result);
        }
        // 可抛出异常或非内部异常（内部异常不能透出）
        if (!(t instanceof NestedRuntimeException) || t instanceof ThrowableRuntimeException) {
            rpcResult.setException(t);
        }

        return rpcResult;
    }
}

