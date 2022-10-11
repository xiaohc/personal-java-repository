package org.example.xhc.common.validation;

import org.example.xhc.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.common.error.ErrorEnum.REQUEST_ERROR;
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
        Executable validate = () -> {
            Expect.isTrue(false)
                    .throwIfFailed(REQUEST_ERROR.becauseOf("yes"));
        };

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 请求内容错误" +
                LINE_SEPARATOR + ">>> The error code is 400" +
                LINE_SEPARATOR + ">>> The reason for the error is yes");
    }
}