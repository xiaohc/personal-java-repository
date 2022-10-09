package org.example.xhc.common.helper;

import lombok.NoArgsConstructor;
import org.example.xhc.common.error.ErrorContext;
import org.example.xhc.common.error.IErrorThrowable;

import java.util.Optional;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@NoArgsConstructor
public class ValidationResult {
    private Optional<ValidatingTuple> validatingTuple = Optional.empty();

    public void throwError(final IErrorThrowable error) {
        validatingTuple.ifPresent(v -> {
            throw ErrorContext.instance().reset().mark(error).becauseOf(buildErrorReason(v)).toException();
        });
    }

    private String buildErrorReason(ValidatingTuple tuple) {
        return tuple.toString();
    }
}
