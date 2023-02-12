/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */
package org.example.xhc.demo.consumer.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 请求内容
 *
 * @author xiaohongchao
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO implements Serializable {
    private static final long serialVersionUID = 2233423128101302462L;

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
     */
    @NotNull(message = "请求时间不能为空")
    private LocalDateTime requestTime;
}
