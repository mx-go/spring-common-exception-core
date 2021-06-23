package com.github.mx.exception.core.annotation;

import com.github.mx.exception.core.ExceptionHandlerConfigureSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * Annotate to the application startup class
 * <p>
 * Create by max on 2021/06/22
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ExceptionHandlerConfigureSelector.class)
public @interface EnableExceptionHandler {

    /**
     * AspectJ pointcut expression
     * e.g：(execution(* com.mx.core..*.*(..)))
     */
    String expression();

    /**
     * AOP order
     */
    int order() default Ordered.LOWEST_PRECEDENCE;

    /**
     * Indicate how caching advice should be applied. The default is
     * {@link AdviceMode#PROXY}.
     *
     * @see AdviceMode
     */
    AdviceMode mode() default AdviceMode.PROXY;
}
