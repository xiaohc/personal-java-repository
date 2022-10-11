package org.example.xhc.common.validation;

import lombok.Builder;
import lombok.Data;
import org.example.xhc.common.error.ErrorEnum;
import org.example.xhc.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.common.validation.BusinessValidate.validate;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试校验工具类的使用
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class BusinessValidateTest {

    @Test
    void checkErrors() {
        RequestDTO request = RequestDTO.builder().build();

        Executable validate = () -> {
            validate(request).throwIfWrong(ErrorEnum.REQUEST_ERROR);
        };

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> 请求内容错误" +
                LINE_SEPARATOR + ">>> The error code is 400" +
                LINE_SEPARATOR + ">>> The reason for the error is requestTime:请求时间不能为空ip:请求IP不能为空");
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