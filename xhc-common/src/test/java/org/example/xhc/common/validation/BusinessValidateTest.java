package org.example.xhc.common.validation;

import org.example.xhc.common.error.ErrorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class BusinessValidateTest {

    @Test
    void checkErrors() {
        BusinessValidate.checkErrors(null).throwIfWrong(ErrorEnum.REQUEST_ERROR);
    }
}