package org.example.xhc.common.validation;

import lombok.Builder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.xhc.common.error.ErrorContext;
import org.example.xhc.common.error.IErrorDescribable;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public static <T> ValidationResult checkErrors(T t) {
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
    public static <T> ValidationResult checkErrors(T t, Class<?>... groups) {
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
    public static <T> ValidationResult validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(t, groups);
        return buildValidationResult(constraintViolations);
    }

    /**
     * 将异常结果封装返回
     *
     * @param <T>
     * @param validateSet
     * @return
     */
    private static <T> ValidationResult buildValidationResult(Set<ConstraintViolation<T>> validateSet) {
        if (CollectionUtils.isEmpty(validateSet)) {
            return ValidationResult.DEFAULT_SUCCESS;
        }

        ValidationResult.ValidationResultBuilder resultBuilder = ValidationResult.builder().hasErrors(true);

        Map<String, String> errorMsgMap = new HashMap<>();
        for (ConstraintViolation<T> constraintViolation : validateSet) {
            errorMsgMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }

        return resultBuilder.errorMsg(errorMsgMap).build();

    }

    /**
     * 校验结果
     */
    @Builder
    public static class ValidationResult {

        /**
         * 校验正常常量
         */
        private static final ValidationResult DEFAULT_SUCCESS = new ValidationResult(false, null);

        /**
         * 是否有异常
         */
        private boolean hasErrors;

        /**
         * 异常消息记录
         */
        private Map<String, String> errorMsg;

        /**
         * 如果校验结果为有异常，则抛出业务错误
         *
         * @param errorDescribable 错误定义
         */
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
            if (errorMsg == null || errorMsg.isEmpty()) {
                return StringUtils.EMPTY;
            }
            StringBuilder message = new StringBuilder();
            errorMsg.forEach((key, value) ->
                message.append(MessageFormat.format("{0}:{1}", key, value))
            );
            return message.toString();
        }
    }
}
