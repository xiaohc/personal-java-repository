package org.example.xhc.common.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应答内容
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum implements IResponseContent {
    /**
     * 处理成功
     */
    SUCCESS(0, "成功"),
    ;

    /**
     * 应答码
     */
    private final Integer code;

    /**
     * 应答消息
     */
    private final String message;
}
