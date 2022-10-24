/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.validation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.xhc.demo.base.reply.ErrorContext;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.common.util.SetUtils.isNotOnlyNullElement;
import static org.example.xhc.demo.base.reply.ErrorEnum.INTERNAL_SERVER_ERROR;

/**
 * 业务验证工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class Expect {
    /**
     * Java Bean 校验器
     */
    private static final Validator VALIDATOR;

    static {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
                .failFast(false)
                .buildValidatorFactory();
        VALIDATOR = validatorFactory.getValidator();
    }

    /**
     * 防止实例化
     */
    private Expect() {
    }

    /**
     * 通过组来校验实体类
     *
     * @param t      实体类对象
     * @param groups 校验分组
     * @param <T>    实体类类型
     * @return 校验结果
     */
    public static <T> IExpectHandle passValidation(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> validatedSet = VALIDATOR.validate(t, groups);

        return error -> {
            if (isNotEmpty(validatedSet) && isNotOnlyNullElement(validatedSet)) {
                throw ensureToException(error, "JSR-303 bean validation").reason(generateErrorDetails(validatedSet)).toException();
            }
        };
    }

    /**
     * 获取组装好的校验异常详情
     *
     * @return 校验异常详情
     */
    private static <T> String generateErrorDetails(Set<ConstraintViolation<T>> validatedSet) {
        String errorMessage = validatedSet.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .collect(groupingBy(
                        ConstraintViolation::getRootBean,
                        mapping(v -> "(" + v.getPropertyPath() + ")" + v.getMessage(), joining(", "))))
                .entrySet()
                .stream()
                .map(v -> ">>>     " + v.getKey() + LINE_SEPARATOR + ">>>       └── " + v.getValue())
                .collect(joining(LINE_SEPARATOR));

        return "JSR-303 bean validation failed, details are as follows:" +
                LINE_SEPARATOR +
                errorMessage;
    }

    /**
     * 验证参数条件是否为true，如果条件表达式的计算结果为false，抛出免检异常
     *
     * @param expression 一个布尔表达式
     * @return 一个函数接口实现
     * @throws RuntimeException 如果表达式为false，抛出免检异常
     */
    public static IExpectHandle isTrue(final boolean expression) {
        return error -> {
            if (!expression) {
                throw ensureToException(error, "expression").toException();
            }
        };
    }

    /**
     * 验证对象是null，如果对象不为null，抛出免检异常
     *
     * @param object 对象
     * @return 一个函数接口实现
     * @throws RuntimeException 如果对象不为null，抛出免检异常
     */
    public static IExpectHandle isNull(final Object object) {
        return error -> {
            if (object != null) {
                throw ensureToException(error, object).toException();
            }
        };
    }

    /**
     * 验证对象不为null，如果对象是null，抛出免检异常
     *
     * @param object 对象
     * @return 一个函数接口实现
     * @throws RuntimeException 如果对象是null，抛出免检异常
     */
    public static IExpectHandle notNull(final Object object) {
        return error -> {
            if (object == null) {
                throw ensureToException(error, "null object").toException();
            }
        };
    }

    /**
     * 验证文本为空，如果文本不为空，抛出免检异常
     *
     * @param text 要检查的字符串
     * @return 一个函数接口实现
     * @throws RuntimeException 如果文本不为空，抛出免检异常
     */
    public static IExpectHandle isBlank(final String text) {
        return error -> {
            if (!StringUtils.isBlank(text)) {
                throw ensureToException(error, text).toException();
            }
        };
    }

    /**
     * 验证文本不为空，如果文本为空，抛出免检异常
     *
     * @param text 要检查的字符串
     * @return 一个函数接口实现
     * @throws RuntimeException 如果文本为空，抛出免检异常
     */
    public static IExpectHandle isNotBlank(final String text) {
        return error -> {
            if (!StringUtils.isNotBlank(text)) {
                throw ensureToException(error, "blank text").toException();
            }
        };
    }

    /**
     * 验证数组不为空，如果数组为空，抛出免检异常
     *
     * @param object 要检查的对象，支持集合、数组、Map
     * @return 一个函数接口实现
     * @throws RuntimeException 如果数组为空，抛出免检异常
     */
    public static IExpectHandle notEmpty(final Object object) {
        return error -> {
            if (ObjectUtils.isEmpty(object)) {
                throw ensureToException(error, object).toException();
            }
        };
    }

    /**
     * 确保能转化为异常
     *
     * @param error  错误内容
     * @param object 对象信息
     * @return 业务异常
     */
    private static ErrorContext ensureToException(ErrorContext error, Object object) {
        return Objects.nonNull(error) ? error
                : INTERNAL_SERVER_ERROR.as("It is necessary to clarify the business error after the business check fails, The check target is {}", object);
    }
}
