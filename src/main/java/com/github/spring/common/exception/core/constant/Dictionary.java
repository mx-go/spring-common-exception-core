package com.github.spring.common.exception.core.constant;

/**
 * 字典接口
 */
public interface Dictionary {
    /**
     * @return 字典代码
     */
    String getCode();

    /**
     * @return 字典名称
     */
    String getName();

    /**
     * 判断字典代码是否相同
     *
     * @param code 字典代码
     * @return true or false
     */
    default boolean equalsCode(String code) {
        return getCode().equals(code);
    }
}