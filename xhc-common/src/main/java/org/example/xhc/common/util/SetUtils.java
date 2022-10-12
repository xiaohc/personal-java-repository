package org.example.xhc.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.Set;

/**
 * set集合工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class SetUtils {
    /**
     * 判断 set集合，仅包含null元素
     *
     * @param collection 集合
     * @param <T>        集合元素类型
     * @return true - 是，false - 否
     */
    public static <T> boolean isOnlyNullElement(Set<T> collection) {
        return CollectionUtils.isEqualCollection(collection, Collections.singleton(null));
    }

    /**
     * 判断 set集合，不是仅包含null元素
     *
     * @param collection 集合
     * @param <T>        集合元素类型
     * @return true - 是，false - 否
     */
    public static <T> boolean isNotOnlyNullElement(Set<T> collection) {
        return !isOnlyNullElement(collection);
    }

    /**
     * 防止外部实例化
     */
    private SetUtils() {
    }
}
