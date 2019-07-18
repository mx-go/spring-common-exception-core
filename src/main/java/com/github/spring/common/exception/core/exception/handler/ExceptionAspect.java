package com.github.spring.common.exception.core.exception.handler;

import com.github.spring.common.exception.core.constant.enums.ArgumentResponseEnum;
import com.github.spring.common.exception.core.constant.enums.CommonResponseEnum;
import com.github.spring.common.exception.core.exception.BaseException;
import com.github.spring.common.exception.core.pojo.response.QR;
import com.github.spring.common.exception.core.pojo.response.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;

/**
 * aop异常拦截
 * 针对service layer
 **/
@Component
@Slf4j
public class ExceptionAspect {

    /**
     * 通知环绕
     *
     * @param point 切点
     * @return object
     * @throws Throwable Throwable
     */
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        Class returnType = ((MethodSignature) signature).getReturnType();
        try {
            return point.proceed();
        } catch (ConstraintViolationException e) {
            // 校验异常
            log.error("参数校验异常", e);
            return wrapperValidateResult(e, returnType);
        } catch (BaseException e) {
            // 业务异常
            log.error(e.getMessage(), e);
            return returnType(e, e.getResponseEnum().getCode(), e.getMessage(), returnType);
        } catch (Exception e) {
            // 未定义异常
            log.error(e.getClass().getName(), e);
            return returnType(e, CommonResponseEnum.SERVER_ERROR.getCode(), e.getClass().getName(),
                    returnType);
        }
    }

    /**
     * 包装校验异常结果
     *
     * @param e          绑定异常
     * @param returnType 返回类型
     * @return 异常结果
     */
    private Object wrapperValidateResult(ConstraintViolationException e, Class returnType) throws Exception {
        StringBuilder msg = new StringBuilder();

        e.getConstraintViolations().forEach(constraintViolation -> {
            msg.append(", ");
            msg.append(constraintViolation.getMessage() == null ? "" : constraintViolation.getMessage());
        });

        return returnType(e, ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2), returnType);
    }

    /**
     * 判断returnType，返回正确类型
     *
     * @param code       错误码
     * @param msg        错误信息
     * @param returnType 返回类型
     * @return 错误信息
     */
    private Object returnType(Exception e, int code, String msg, Class returnType) throws Exception {
        if (R.class.equals(returnType)) {
            return new R(code, msg);
        }
        if (QR.class.equals(returnType)) {
            return new QR(code, msg);
        }
        throw e;
    }
}