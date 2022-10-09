package org.example.xhc.common.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 校验元组
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class ValidatingTuple {
    /**
     * 是否有异常
     */
    private boolean hasErrors;

    /**
     * 异常消息记录
     */
    private Map<String, String> errorMsg;
}
