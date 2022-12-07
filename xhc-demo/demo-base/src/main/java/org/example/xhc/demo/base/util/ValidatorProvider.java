/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 公用校验器
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class ValidatorProvider {

    /**
     * Java Bean 校验器
     */
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
                .failFast(false)
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * 防止实例化
     */
    private ValidatorProvider() {
    }

    /**
     * 获取校验器
     *
     * @return 校验器
     */
    public static Validator get() {
        return validator;
    }

    /**
     * 重置校验器
     *
     * @param validator 校验器
     */
    public static void setValidator(Validator validator) {
        ValidatorProvider.validator = validator;
    }
}
