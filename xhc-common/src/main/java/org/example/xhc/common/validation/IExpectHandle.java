package org.example.xhc.common.validation;

import org.example.xhc.common.error.IErrorDescribable;

/**
 * 定义如何处理期望
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@FunctionalInterface
public interface IExpectHandle {
    /**
     * 如果期望结果为false，抛出免检异常
     *
     * @param error 错误
     */
    void throwIfFailed(IErrorDescribable error);
}
