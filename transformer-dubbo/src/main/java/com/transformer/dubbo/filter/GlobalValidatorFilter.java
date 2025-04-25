package com.transformer.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.transformer.validate.Validation;
import com.transformer.helper.JsonHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Activate(group = Constants.PROVIDER, order = Integer.MAX_VALUE)
public class GlobalValidatorFilter implements Filter {

    /**
     * 注入校验器实现
     *
     * @param validator
     */
    @Setter
    private Validator validator;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            if (needValidator(invocation)) {
                // 参数校验
                Set<ConstraintViolation<Object>> validateResult = validate(invocation.getArguments());

                // 提取校验失败消息
                String message = extractMessage(validateResult);

                // 没有校验失败消息
                Assert.isTrue(StringUtils.isBlank(message), message);
            }
        } catch (Exception e) {
            log.error("validate arguments error:{}", JsonHelper.toJson(invocation.getArguments()), e);

            // 非法参数异常，抛出
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }
        }

        // 验证成功
        return invoker.invoke(invocation);
    }

    private boolean needValidator(Invocation invocation) throws NoSuchMethodException {
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        Method method = invocation.getInvoker().getInterface().getMethod(invocation.getMethodName(), parameterTypes);

        // 方法签名支持@Valid注解
        if (isMethodAnnotationValid(method)) {
            return true;
        }

        // 参数签名是否有@Valid注解
        if (isParameterAnnotationValid(method.getParameterAnnotations())) {
            return true;
        }

        // 参数类是否有@Valid注解或实现了Validatable
        return isParameterNeedValid(invocation.getArguments());
    }

    protected boolean isMethodAnnotationValid(Method method) {
        // 方法有@Valid注解
        return method.getAnnotation(Valid.class) != null;
    }

    protected boolean isParameterAnnotationValid(Annotation[][] parameterAnnotations) {
        // 参数有@Valid注解
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Valid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParameterNeedValid(Object[] arguments) {
        if (ArrayUtils.isEmpty(arguments)) {
            return false;
        }

        for (Object argument : arguments) {
            // 参数对象有@Valid 或 对象是Validation实例
            if (isObjectAnnotationValid(argument) || isObjectInstanceOfValidation(argument)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isObjectAnnotationValid(Object argument) {
        if (Objects.isNull(argument)) {
            return false;
        }

        // 参数对象是否开启校验
        Annotation[] annotations = argument.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Valid) {
                return true;
            }
        }
        return false;
    }

    protected boolean isObjectInstanceOfValidation(Object argument) {
        // 参数对象是否实现 Verifiable
        return Validation.class.isAssignableFrom(argument.getClass());
    }

    private Set<ConstraintViolation<Object>> validate(Object[] arguments) {
        if (ArrayUtils.isEmpty(arguments)) {
            return Collections.emptySet();
        }

        return Arrays.stream(arguments)
                .map(this::validate)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<ConstraintViolation<Object>> validate(Object argument) {
        if (Objects.isNull(argument)) {
            return Collections.emptySet();
        }

        Set<ConstraintViolation<Object>> result = standardValidate(argument);
        if (result != null) return result;

        // 执行自定义校验
        customValidate(argument);

        return Collections.emptySet();
    }

    protected Set<ConstraintViolation<Object>> standardValidate(Object argument) {
        try {
            // 执行validator校验
            Set<ConstraintViolation<Object>> result = getValidator().validate(argument);
            if (CollectionUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            // validator执行时的异常，不阻断操作
            log.error("validate error argument:{}", JsonHelper.toJson(argument), e);
        }
        return null;
    }

    protected void customValidate(Object argument) {
        ((Validation) argument).validator();
    }

    /**
     * 获取validator校验器
     *
     * @return
     */
    private Validator getValidator() {
        return this.validator;
    }

    /**
     * 提取所有校验失败的文案
     *
     * @param validateResult
     * @return
     */
    private String extractMessage(Set<ConstraintViolation<Object>> validateResult) {
        if (CollectionUtils.isEmpty(validateResult)) {
            return StringUtils.EMPTY;
        }

        return validateResult.stream()
                .map(ConstraintViolation::getMessage)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(";"));
    }

}
