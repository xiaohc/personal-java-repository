/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base.checker;

import org.example.xhc.common.base.IRecordable;

import java.util.Optional;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IAssertAble extends IRecordable {
    default Optional<IRecordable> assertIsTrue(boolean expression) {
        if (expression) {
            return Optional.of(this);
        }
        return Optional.empty();
    }
}
