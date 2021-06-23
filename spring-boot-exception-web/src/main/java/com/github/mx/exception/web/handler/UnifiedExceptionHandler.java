package com.github.mx.exception.web.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.mx.exception.common.beans.response.PR;
import com.github.mx.exception.common.beans.response.R;
import com.github.mx.exception.common.constant.CommonErrorCode;
import com.github.mx.exception.common.constant.ExceptionConst;
import com.github.mx.exception.common.exception.BaseException;
import com.github.mx.exception.common.exception.BusinessException;
import com.github.mx.exception.web.enums.ServletResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * Controller全局异常处理器
 * <p>
 * Create by max on 2021/06/23
 */
@Slf4j
@Component
@ControllerAdvice
public class UnifiedExceptionHandler {

    public static String APP_NAME;

    /**
     * 生产环境标识
     */
    private static final String ENV_PROD = "prod";

    /**
     * 当前环境
     * default:dev
     */
    @Value("${spring.profiles.active:dev}")
    private String profile;

    public UnifiedExceptionHandler(StandardEnvironment environment) {
        APP_NAME = environment.resolvePlaceholders("${spring.application.name:}");
    }

    /**
     * 可在此处理国际化消息
     *
     * @param e 异常
     * @return 错误信息
     */
    public String getMessage(BaseException e) {
        return e.getMessage();
    }

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public PR<Void> handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);
        String traceMsg = getTraceMsg(e.getResponseEnum().getTraceMessage(), e.getMessage());
        return new PR<>(e.getResponseEnum().getCode(), getMessage(e), traceMsg);
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public PR<Void> handleBaseException(BaseException e) {
        log.warn(e.getMessage(), e);
        String traceMsg = getTraceMsg(e.getResponseEnum().getTraceMessage(), e.getMessage());
        return new PR<>(e.getResponseEnum().getCode(), getMessage(e), traceMsg);
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public PR<Void> handleServletException(Exception e) {
        log.warn(e.getMessage(), e);
        int code = CommonErrorCode.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            log.error("Class [{}] not defined in Enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }

        String traceMsg = String.format(ExceptionConst.TRACE_MESSAGE_PATTERN, APP_NAME, e.getMessage());
        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
            code = CommonErrorCode.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonErrorCode.SERVER_ERROR);
            String message = getMessage(baseException);
            return new PR<>(code, message, traceMsg);
        }
        return new PR<>(code, e.getMessage(), traceMsg);
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public R<Void> handleValidException(MethodArgumentNotValidException e) {
        log.info("Argument valid exception.", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private R<Void> wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }
        return new R<>(CommonErrorCode.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public PR<Void> handleValidException(ConstraintViolationException e) {
        log.info("Argument valid exception", e);
        StringBuilder msg = new StringBuilder();
        e.getConstraintViolations().forEach(constraintViolation -> {
            msg.append(", ");
            msg.append(constraintViolation.getMessage() == null ? "" : constraintViolation.getMessage());
        });
        return new PR<>(CommonErrorCode.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public PR<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        String traceMsg = String.format(ExceptionConst.TRACE_MESSAGE_PATTERN, APP_NAME, e.getClass().getName());
        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            int code = CommonErrorCode.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonErrorCode.SERVER_ERROR);
            String message = getMessage(baseException);
            return new PR<>(code, message, traceMsg);
        }
        return new PR<>(CommonErrorCode.SERVER_ERROR.getCode(), e.getMessage(), traceMsg);
    }

    private String getTraceMsg(String traceMsg, String message) {
        if (StringUtils.isNotBlank(traceMsg)) {
            return traceMsg;
        }
        return String.format(ExceptionConst.TRACE_MESSAGE_PATTERN, APP_NAME, message);
    }
}