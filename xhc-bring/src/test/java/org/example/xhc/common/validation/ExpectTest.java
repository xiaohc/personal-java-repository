package org.example.xhc.common.validation;

import org.example.xhc.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.common.reply.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 期望工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ExpectTest {

    @Test
    void isTrue() {
        Executable validate = () -> Expect.isTrue(false).throwIfFailed(INTERNAL_SERVER_ERROR.as("yes"));

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 系统内部错误" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> yes");
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

}