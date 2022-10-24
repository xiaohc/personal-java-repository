/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.helper;

import org.example.xhc.demo.base.exception.BusinessException;
import org.example.xhc.demo.base.validation.Expect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.demo.base.reply.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class IfTest {

    @Test
    void isTrue() {
        Executable validate = () ->
                If.isTrue(true).thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 系统内部错误" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }

    @Test
    void isNull() {
    }

    @Test
    void notNull() {
        Executable validate = () -> Expect.notNull(null)
                .throwIfFailed(INTERNAL_SERVER_ERROR.as("测试业务检查工具类: {}", "Validate"));

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 系统内部错误" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试业务检查工具类: Validate");
    }

    @Test
    void isBlank() {
    }

    @Test
    void isNotBlank() {
    }
}