package com.github.mx.exception.core;

import com.github.mx.exception.common.config.JacksonConfiguration;
import com.github.mx.exception.core.annotation.EnableExceptionHandler;
import com.github.mx.exception.core.handler.ExceptionAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启动配置类
 * <p>
 * Create by max on 2021/06/22
 **/
@Configuration
@Slf4j
public class ExceptionHandlerAutoConfiguration implements ImportAware {

    private AnnotationAttributes enableAnnotation;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        enableAnnotation = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableExceptionHandler.class.getName(), false));
        if (enableAnnotation == null) {
            log.info("@EnableExceptionHandler is not present on importing class");
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public AspectJExpressionPointcutAdvisor exceptionHandlerAdvisor(StandardEnvironment environment) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(enableAnnotation.getString("expression"));
        advisor.setOrder(enableAnnotation.getNumber("order"));
        advisor.setAdvice(exceptionAspect(environment));
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ExceptionAspect exceptionAspect(StandardEnvironment environment) {
        return new ExceptionAspect(environment);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public JacksonConfiguration jacksonConfiguration() {
        return new JacksonConfiguration();
    }
}