package org.example.xhc.demo.base.validation;

import lombok.Builder;
import lombok.Data;
import org.example.xhc.demo.base.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.demo.base.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.demo.base.reply.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.example.xhc.demo.base.validation.Expect.passValidation;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试校验工具类的使用
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ValidateTest {

    @Test
    void checkErrors() {
        Executable validate = () -> {
            RequestDTO request = RequestDTO.builder().build();
            passValidation(request).throwIfFailed(INTERNAL_SERVER_ERROR.as("test"));
        };

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 系统内部错误" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> test" +
                LINE_SEPARATOR + ">>> JSR-303 bean validation failed, details are as follows:" +
                LINE_SEPARATOR + ">>>     ValidateTest.RequestDTO(ip=null, requestTime=null)" +
                LINE_SEPARATOR + ">>>       └── (ip)请求IP不能为空, (requestTime)请求时间不能为空");
    }

    @Data
    @Builder
    private static class RequestDTO {
        /**
         * 请求IP
         */
        @NotBlank(message = "请求IP不能为空")
        @Pattern(regexp = "(?:(?:1[0-9][0-9]\\.)|(?:2[0-4][0-9]\\.)|(?:25[0-5]\\.)|(?:[1-9][0-9]\\.)|(?:[0-9]\\.)){3}"
                + "(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))",
                message = "请求IP必须是有效的IPv4地址")
        private String ip;

        /**
         * 请求时间
         * example = "2022-01-15T09:10:47.507Z"
         */
        @NotNull(message = "请求时间不能为空")
        private LocalDateTime requestTime;

    }
}