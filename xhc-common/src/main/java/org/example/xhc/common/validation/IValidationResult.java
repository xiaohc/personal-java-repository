package org.example.xhc.common.validation;

import org.example.xhc.common.error.IErrorDefinable;

/**
 * 校验结果定义
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IValidationResult {
    /**
     * 如果校验结果为有异常，则抛出业务错误
     *
     * @param errorDescribable 错误定义
     */
    void throwIfWrong(IErrorDefinable errorDescribable);
}
