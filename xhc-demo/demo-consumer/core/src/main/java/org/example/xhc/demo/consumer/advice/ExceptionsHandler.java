/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */
package org.example.xhc.demo.consumer.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.xhc.demo.base.util.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.example.xhc.demo.base.common.ErrorEnum.INTERNAL_SERVER_ERROR;

/**
 * 统一异常处理
 *
 * @author xiaohongchao
 * @since 1.0
 */
@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
    /**
     * 请求参数校验不通过异常
     *
     * @param exception 异常信息
     * @return 响应实体
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerException(MethodArgumentNotValidException exception) {
        log.warn("Found exception.", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Result.failure(INTERNAL_SERVER_ERROR.as(HttpStatus.BAD_REQUEST.getReasonPhrase())));
    }

    /**
     * 异常兜底处理
     *
     * @param exception 异常信息
     * @return 响应实体
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception exception) {
        log.error("Found unknown exception.", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.failure(INTERNAL_SERVER_ERROR.as(HttpStatus.BAD_REQUEST.getReasonPhrase())));
    }
}
