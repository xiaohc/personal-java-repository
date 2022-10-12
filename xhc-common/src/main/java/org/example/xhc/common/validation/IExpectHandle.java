package org.example.xhc.common.validation;

import org.example.xhc.common.error.ErrorContext;

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
     * @param errorContext 错误上下文
     */
    void throwIfFailed(ErrorContext errorContext);
}
