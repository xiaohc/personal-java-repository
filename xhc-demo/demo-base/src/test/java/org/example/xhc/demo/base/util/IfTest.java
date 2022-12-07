/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.example.xhc.demo.base.exception.BusinessException;
import org.example.xhc.demo.base.util.If;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.demo.base.common.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 工具类单元测试
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

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is BASE-COMM-999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }

    @Test
    void isNull() {
        Executable validate = () -> If.isNull(null)
                .thenThrow(INTERNAL_SERVER_ERROR.as("测试业务检查工具类: {}", "Validate"));

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is BASE-COMM-999" +
                LINE_SEPARATOR + ">>> 测试业务检查工具类: Validate");
    }

    @Test
    void notNull() {
        Executable validate = () -> If.notNull(new Object())
                .thenThrow(INTERNAL_SERVER_ERROR.as("测试业务检查工具类: {}", "Validate"));

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is BASE-COMM-999" +
                LINE_SEPARATOR + ">>> 测试业务检查工具类: Validate");
    }

    @Test
    void isBlank() {
        Executable validate = () -> If.isBlank(" ")
                .thenThrow(INTERNAL_SERVER_ERROR.as("测试业务检查工具类: {}", "Validate"));

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is BASE-COMM-999" +
                LINE_SEPARATOR + ">>> 测试业务检查工具类: Validate");
    }

    @Test
    void isNotBlank() {
        Executable validate = () -> If.isNotBlank("a ")
                .thenThrow(INTERNAL_SERVER_ERROR.as("测试业务检查工具类: {}", "Validate"));

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is BASE-COMM-999" +
                LINE_SEPARATOR + ">>> 测试业务检查工具类: Validate");
    }
}