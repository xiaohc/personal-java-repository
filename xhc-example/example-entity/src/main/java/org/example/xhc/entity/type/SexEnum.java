/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum SexEnum {
    /**
     * 性别男
     */
    MALE(1, "男", "male"),

    /**
     * 性别女
     */
    FEMALE(2, "女", "female");

    /**
     * 标识值
     */
    private final int value;

    /**
     * 中文名称
     */
    private final String nameCn;

    /**
     * 英文名称
     */
    private final String nameEn;
}
