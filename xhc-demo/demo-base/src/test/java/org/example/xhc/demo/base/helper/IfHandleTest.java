/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.helper;

import lombok.val;
import org.example.xhc.demo.base.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.demo.base.reply.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.example.xhc.demo.base.util.Check.isNull;
import static org.example.xhc.demo.base.util.Check.isTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 工具类单元测试
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class IfHandleTest {

    @Test
    void testOr1() {
        Executable validate = () ->
                If.isNull(null)
                        .or(isTrue(false))
                        .thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 系统内部错误" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }

    @Test
    void testOr2() {
        Executable validate = () ->
        {
            val businessException = new BusinessException(null);
            If.isNull(businessException)
                    .or(isNull(businessException.getErrorContext()))
                    .thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));
        };

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 系统内部错误" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }
}