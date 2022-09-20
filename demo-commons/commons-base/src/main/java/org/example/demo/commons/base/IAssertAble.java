/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.demo.commons.base;

import java.util.Optional;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IAssertAble extends IErrorContext {
    default Optional<IErrorContext> assertIsTrue(boolean expression) {
        if (expression) {
            return Optional.of(this);
        }
        return Optional.empty();
    }
}
