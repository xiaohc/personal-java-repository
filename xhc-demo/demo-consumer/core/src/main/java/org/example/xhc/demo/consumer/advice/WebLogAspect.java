/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */
package org.example.xhc.demo.consumer.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Web 日志切面
 *
 * @author xiaohongchao
 * @since 1.0
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    /**
     * 调用时间记录器
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 日志 pointcut
     */
    @Pointcut("execution(public * org.example.xhc.demo.consumer.controller..*.*(..))")
    public void webLog() {
        // do nothing
    }

    /**
     * 调用前记录日志
     *
     * @param joinPoint 连接点
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "Request content cannot be empty");
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("(doBefore) IP : {}, URL: {}, CLASS_METHOD : {}, ARGS : {}",
                request.getRemoteAddr(),
                request.getRequestURL(),
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 返回后记录日志
     *
     * @param ret 返回内容
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        log.info("(doAfterReturning) SPEND TIME : {}, RESPONSE : {}",
                (System.currentTimeMillis() - startTime.get()),
                ret);
        startTime.remove();
    }
}
