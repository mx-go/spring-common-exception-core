package com.github.spring.common.exception.core.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <pre>
 *  Stream 工具类
 * </pre>
 */
@UtilityClass
public class StreamUtils {

    /**
     * 简版{@link Collectors#toMap(Function, Function)}, 提供 keyMapper, valueMapper 为{@link Function#identity()}.
     * {@link Function#identity()} 等价于: t - t
     *
     * @param coll      源集合
     * @param keyMapper 生成key的{@link Function}
     * @param <K>       Map的键类型
     * @param <T>       Map的值类型
     * @return 键值对
     */
    public <K, T> Map<K, T> toMap(Collection<T> coll, Function<? super T, ? extends K> keyMapper) {
        if (coll == null || coll.size() == 0) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * 取反
     *
     * @param target 待取反
     * @param <T>    T
     * @return 取反结果
     */
    public <T> Predicate<T> not(Predicate<T> target) {
        return target.negate();
    }
}
