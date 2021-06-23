package com.github.mx.exception.core;

import com.github.mx.exception.core.annotation.EnableExceptionHandler;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * Create by max on 2021/06/22
 **/
public class ExceptionHandlerConfigureSelector extends AdviceModeImportSelector<EnableExceptionHandler> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{ExceptionHandlerAutoConfiguration.class.getName()};
    }
}
