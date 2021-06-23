package com.github.mx.exception.core.handler;

import com.github.mx.exception.common.beans.response.R;
import com.github.mx.exception.common.constant.CommonErrorCode;
import com.github.mx.exception.common.exception.ArgumentException;
import com.github.mx.exception.common.exception.BaseException;
import com.github.mx.exception.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * 切面
 * <p>
 * Create by max on 2021/06/22
 */
@Slf4j
public class ExceptionAspect implements MethodInterceptor {

    private static final String TRACE_MSG = "provider:%s, reason:%s";

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator methodValidator = factory.getValidator().forExecutables();
    private final Validator beanValidator = factory.getValidator();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        Object target = invocation.getThis();
        Object[] args = invocation.getArguments();

        R<Void> result = validate(method, target, args);
        if (!result.isSuccess()) {
            return result;
        }

        try {
            return invocation.proceed();
        } catch (ArgumentException ae) {
            log.info("Method ArgumentException.", ae);
            return new R<>(ae.getResponseEnum().getCode(), ae.getMessage());
        } catch (BusinessException be) {
            log.warn("Method BusinessException.", be);
            return new R<>(be.getResponseEnum().getCode(), be.getMessage());
        } catch (BaseException ben) {
            log.warn("Method BaseException.", ben);
            return new R<>(ben.getResponseEnum().getCode(), ben.getMessage());
        } catch (Exception e) {
            log.error("Method Exception.", e);
            return new R<>(CommonErrorCode.SERVER_ERROR.getCode(), e.getClass().getSimpleName());
        }
    }

    /**
     * 校验入参
     */
    public R<Void> validate(Method method, Object target, Object[] args) {
        StringBuilder msg = new StringBuilder();

        // 校验以基本数据类型为方法参数
        validateParameters(target, method, args).forEach(violation -> {
            msg.append(", ");
            msg.append(violation.getMessage() == null ? "" : violation.getMessage());
        });
        // 校验以java bean对象为方法参数
        Arrays.stream(args).filter(Objects::nonNull).forEach(bean -> validate(bean).forEach(violation -> {
            msg.append(", ");
            msg.append(violation.getMessage() == null ? "" : violation.getMessage());
        }));
        return msg.length() != 0 ? new R<>(CommonErrorCode.VALID_ERROR.getCode(), msg.substring(2)) : new R<>();
    }

    private <T> Set<ConstraintViolation<T>> validateParameters(T obj, Method method, Object[] params) {
        return methodValidator.validateParameters(obj, method, params);
    }

    private <T> Set<ConstraintViolation<T>> validate(T bean) {
        return beanValidator.validate(bean);
    }
}