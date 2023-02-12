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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 响应内容
 *
 * @author xiaohongchao
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO implements Serializable {
    private static final long serialVersionUID = -5618000004417550693L;

    /**
     * 响应消息
     */
    @NotBlank(message = "响应消息不能为空")
    private String msg;

    /**
     * 响应时间
     */
    @NotNull(message = "请求时间不能为空")
    private LocalDateTime responseTime;
}