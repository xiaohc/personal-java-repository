/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.reply;

import java.io.Serializable;

/**
 * 代表什么都不做
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class Nothing implements Serializable {

    private static final long serialVersionUID = -3975225228738468763L;

    public static final Nothing INSTANCE = new Nothing();

    private Nothing() {
    }
}