/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */
package org.example.xhc.demo.consumer.controller;

import org.example.xhc.demo.base.util.Result;
import org.example.xhc.demo.consumer.api.RequestDTO;
import org.example.xhc.demo.consumer.api.ResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 消费入口
 *
 * @author xiaohongchao
 * @since 1.0
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    /**
     * 短域名存储
     *
     * @param urlRequestDTO 长域名
     * @return 短域名
     */
    @PostMapping(
            value = "/hello",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<ResponseDTO> sayHello(@RequestBody @Validated RequestDTO urlRequestDTO) {
        final ResponseDTO responseDTO = ResponseDTO.builder()
                .responseTime(LocalDateTime.now())
                .msg("hello world")
                .build();

        return Result.of(responseDTO);
    }
}
