package com.github.spring.common.exception.core.exception.handler;

import com.github.spring.common.exception.core.constant.enums.ArgumentResponseEnum;
import com.github.spring.common.exception.core.constant.enums.CommonResponseEnum;
import com.github.spring.common.exception.core.constant.enums.ServletResponseEnum;
import com.github.spring.common.exception.core.exception.BaseException;
import com.github.spring.common.exception.core.exception.BusinessException;
import com.github.spring.common.exception.core.pojo.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 */
@Slf4j
@Component
@ControllerAdvice
public class UnifiedExceptionHandler {
    /**
     * 生产环境
     */
    @Value("${spring.profiles.prod:prod}")
    private String envProd;

    /**
     * 当前环境
     * default:dev
     */
    @Value("${spring.profiles.active:dev}")
    private String profile;

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
    public ErrorResponse handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);

        return new ErrorResponse(e.getResponseEnum().getCode(), getMessage(e));
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ErrorResponse handleBaseException(BaseException e) {
        log.warn(e.getMessage(), e);

        return new ErrorResponse(e.getResponseEnum().getCode(), getMessage(e));
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
    })
    @ResponseBody
    public ErrorResponse handleServletException(Exception e) {
        log.warn(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }

        if (envProd.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
            code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getMessage(baseException);
            return new ErrorResponse(code, message);
        }

        return new ErrorResponse(code, e.getMessage());
    }


    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ErrorResponse handleBindException(BindException e) {
        log.warn("参数绑定校验异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handleValidException(MethodArgumentNotValidException e) {
        log.warn("参数绑定校验异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ErrorResponse handleValidException(ConstraintViolationException e) {
        log.warn("参数绑定校验异常", e);

        StringBuilder msg = new StringBuilder();
        e.getConstraintViolations().forEach(constraintViolation -> {
            msg.append(", ");
            msg.append(constraintViolation.getMessage() == null ? "" : constraintViolation.getMessage());
        });
        return new ErrorResponse(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private ErrorResponse wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }

        return new ErrorResponse(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e);

        if (envProd.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            int code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getMessage(baseException);
            return new ErrorResponse(code, message);
        }

        return new ErrorResponse(CommonResponseEnum.SERVER_ERROR.getCode(), e.getMessage());
    }

}
