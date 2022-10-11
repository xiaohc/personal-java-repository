package org.example.xhc.common.validation;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.xhc.common.error.ErrorContext;
import org.example.xhc.common.error.IErrorDescribable;
import org.hibernate.validator.HibernateValidator;

import javax.validation.*;
import javax.validation.groups.Default;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.common.util.SetUtils.isOnlyNullElement;

/**
 * 业务验证工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class BusinessValidate {
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
    private BusinessValidate() {
    }

    /**
     * 校验实体类
     *
     * @param t   实体类对象
     * @param <T> 实体类类型
     * @return 校验结果
     */
    public static <T> IValidationResult checkErrors(T t) {
        return validate(t, Default.class);
    }


    /**
     * 通过组来校验实体类
     *
     * @param t      实体类对象
     * @param groups 校验分组
     * @param <T>    实体类类型
     * @return 校验结果
     */
    public static <T> IValidationResult checkErrors(T t, Class<?>... groups) {
        return validate(t, groups);
    }

    /**
     * 通过组来校验实体类
     *
     * @param t      实体类对象
     * @param groups 校验分组
     * @param <T>    实体类类型
     * @return 校验结果
     */
    public static <T> IValidationResult validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(t, groups);
        return buildValidationResult(constraintViolations);
    }

    /**
     * 将异常结果封装返回
     *
     * @param <T>          校验类型
     * @param validatedSet 校验结果的集合
     * @return 校验结果
     */
    private static <T> IValidationResult buildValidationResult(Set<ConstraintViolation<T>> validatedSet) {
        if (isEmpty(validatedSet) || isOnlyNullElement(validatedSet)) {
            return ValidationResultImpl.VALIDATION_SUCCESS;
        }

        Map<Path, String> errorDetails = validatedSet.stream()
                .filter(Objects::nonNull)
                .collect(groupingBy(
                        ConstraintViolation::getPropertyPath,
                        mapping(ConstraintViolation::getMessage, joining(" and ", " : ", "."))));

        return new ValidationResultImpl(true, errorDetails);
    }

    /**
     * 校验结果
     */
    @AllArgsConstructor
    private static class ValidationResultImpl implements IValidationResult {

        /**
         * 校验正常常量
         */
        private static final ValidationResultImpl VALIDATION_SUCCESS = new ValidationResultImpl(false, null);

        /**
         * 是否有异常
         */
        private final boolean hasErrors;

        /**
         * 异常消息记录
         */
        private final Map<Path, String> errorDetails;

        /**
         * 如果校验结果为有异常，则抛出业务错误
         *
         * @param errorDescribable 错误定义
         */
        @Override
        public void throwIfWrong(final IErrorDescribable errorDescribable) {
            if (hasErrors) {
                throw ErrorContext.instance().reset().mark(errorDescribable).becauseOf(getMessage()).toException();
            }
        }

        /**
         * 获取组装好的校验异常详情
         *
         * @return 校验异常详情
         */
        public String getMessage() {
            if (isEmpty(errorDetails)) {
                return StringUtils.EMPTY;
            }

            return errorDetails
                    .entrySet()
                    .stream()
                    .map(v -> v.getKey() + v.getValue())
                    .collect(joining(LINE_SEPARATOR, "JSR-303 Bean Validation." + LINE_SEPARATOR, ""));
        }
    }
}
