/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.reply;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.xhc.demo.base.exception.BusinessException;
import org.example.xhc.demo.base.helper.FormattingTuple;
import org.example.xhc.demo.base.helper.MessageFormatter;

import java.io.Serializable;
import java.util.Optional;

import static org.example.xhc.demo.base.constant.SystemConstants.LINE_SEPARATOR;

/**
 * 错误上下文
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
public class ErrorContext implements Serializable {
    private static final long serialVersionUID = 800312585448987400L;

    /**
     * 性能增强，兼顾多线程
     */
    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误描述
     */
    private String description;

    /**
     * 错误原因
     */
    private String reason;

    /**
     * 如果是异常引起，记录对应异常
     */
    private Throwable cause;

    /**
     * 构成错误链，可以追溯错误源
     */
    private ErrorContext previous;

    /**
     * 防止外部实例化
     */
    private ErrorContext() {
    }

    /**
     * 从ThreadLocal取出已经实例化的ErrorContext，或者实例化一个ErrorContext放入ThreadLocal
     *
     * @return ErrorContext实例
     */
    public static ErrorContext instance() {
        ErrorContext context = LOCAL.get();
        if (context == null) {
            context = new ErrorContext();
            LOCAL.set(context);
        }
        return context;
    }

    /**
     * 创建一个包装了原有ErrorContext的新ErrorContext
     *
     * @return 新的ErrorContext实例
     */
    public ErrorContext store() {
        ErrorContext newContext = new ErrorContext();
        newContext.previous = this;
        LOCAL.set(newContext);
        return LOCAL.get();
    }

    /**
     * 剥离出当前ErrorContext的内部ErrorContext
     *
     * @return 剥离出的ErrorContext对象
     */
    public ErrorContext recall() {
        if (previous != null) {
            LOCAL.set(previous);
            previous = null;
        }
        return LOCAL.get();
    }

    /**
     * 从ThreadLocal中移除
     */
    public void remove() {
        LOCAL.remove();
    }

    /**
     * 重置原ErrorContext内容
     */
    public ErrorContext reset() {
        code = null;
        message = null;
        reason = null;
        cause = null;
        previous = null;
        return this;
    }

    /**
     * 重置原ErrorContext内容为指定内容
     *
     * @param errorRecord 错误记录
     * @return ErrorContext对象
     */
    public ErrorContext reset(final IResponseContent errorRecord) {
        reset();
        Optional.ofNullable(errorRecord)
                .ifPresent(v -> {
                    this.code = v.getCode();
                    this.message = v.getMessage();
                });
        return this;
    }

    /**
     * 设置错误描述
     *
     * @param description 错误描述
     * @return ErrorContext对象
     */
    public ErrorContext description(final String description) {
        this.description = description;
        return this;
    }

    /**
     * 设置错误描述
     *
     * @param descriptionPattern 原因
     * @return ErrorContext对象
     */
    public ErrorContext description(final String descriptionPattern, final Object... params) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(descriptionPattern, params);
        this.description = formattingTuple.getMessage();
        this.cause = formattingTuple.getThrowable();
        return this;
    }

    /**
     * 设置错误原因
     *
     * @param reason 原因
     * @return ErrorContext对象
     */
    public ErrorContext reason(final String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * 设置错误原因
     *
     * @param reasonPattern 原因待格式化字符串
     * @param params        参数（支持最后1个参数为Throwable）
     * @return ErrorContext对象
     */
    public ErrorContext reason(final String reasonPattern, final Object... params) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(reasonPattern, params);
        this.reason = formattingTuple.getMessage();
        this.cause = formattingTuple.getThrowable();
        return this;
    }

    /**
     * 增加错误原因
     *
     * @param reason 原因
     * @return ErrorContext对象
     */
    public ErrorContext appendReason(final String reason) {
        if (StringUtils.isBlank(this.reason)) {
            this.reason = reason;
        } else {
            this.reason = this.reason + LINE_SEPARATOR + reason;
        }
        return this;
    }

    /**
     * 设置引起错误的异常信息
     *
     * @param cause 异常
     * @return ErrorContext对象
     */
    public ErrorContext cause(final Throwable cause) {
        this.cause = cause;
        return this;
    }

    /**
     * 转为业务异常
     *
     * @return 业务异常
     */
    public BusinessException toException() {
        return cause == null
                ? new BusinessException(this)
                : new BusinessException(this, cause);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        // message
        if (message != null) {
            ret.append(LINE_SEPARATOR);
            ret.append(">>> ");
            ret.append(message);
        }

        // code
        if (code != null) {
            ret.append(LINE_SEPARATOR);
            ret.append(">>> The error code is ");
            ret.append(code);
        }

        // description
        if (description != null) {
            ret.append(LINE_SEPARATOR);
            ret.append(">>> ");
            ret.append(description);
        }

        // reason
        if (reason != null) {
            ret.append(LINE_SEPARATOR);
            ret.append(">>> ");
            ret.append(reason);
        }

        // cause
        if (cause != null) {
            ret.append(LINE_SEPARATOR);
            ret.append(">>> Cause: ");
            ret.append(cause);
        }

        return ret.toString();
    }
}
