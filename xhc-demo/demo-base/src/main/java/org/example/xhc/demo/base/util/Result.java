/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.example.xhc.demo.base.common.IResultEnum;
import org.example.xhc.demo.base.exception.BusinessException;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.example.xhc.demo.base.common.ErrorEnum.*;

/**
 * 容器类 : 表示可能会失败的操作，其操作结果为有数据或错误
 * <p>
 * Result.Success 子类型（表示操作成功）
 * Result.Failure 子类型（表示操作失败，包含错误信息）
 * <p>
 * 容器存储元素：value数据（操作成功）
 * <p>
 * 注意：部分方法使用函数式编程概念
 *
 * @param <T> 预期正常返回的数据类型
 * @author xiaohongchao
 * @see java.util.Optional
 * @see java.util.List
 * @since 1.0.0
 */
public interface Result<T> extends Serializable {

    /**
     * 判断操作结果
     *
     * @return true -操作成功
     */
    Boolean isSuccess();

    /**
     * 判断操作结果
     *
     * @return true - 操作失败
     */
    Boolean isFailure();

    /**
     * 如果 Result 为 Success，返回 Result容器存储的数据（value）
     * 如果 Result 为 Failure，则抛出异常
     *
     * @return 容器存储数据（value）
     */
    T successValue();

    /**
     * 如果 Result 为 Failure，返回 返回错误上下文
     * 如果 Result 为 Success，则抛出异常
     *
     * @return 错误上下文
     */
    ErrorContext failureValue();

    /**
     * 如果 Result 为 Success，返回 Result容器存储的数据（value）
     * 如果 Result 为 Failure，则返回空
     *
     * @return 容器存储数据（value）
     */
    T get();

    /**
     * 如果 Result 为 Success，返回 Result容器存储的数据（value）
     * 如果 Result 为 Success.EMPTY 或 Failure，返回缺省值
     *
     * @param defaultValue 缺省值
     * @return 容器存储数据（value）
     */
    default T getOrElse(final T defaultValue) {
        return get() != null ? get() : defaultValue;
    }

    /**
     * 如果 Result 为 Success，返回 Result容器存储的数据（value）
     * 如果 Result 为 Success.EMPTY 或 Failure，返回缺省值
     *
     * @param s 缺省值生成函数
     * @return 容器存储数据（value）
     */
    default T getOrElse(final Supplier<T> s) {
        Objects.requireNonNull(s);
        return get() != null ? get() : s.get();
    }

    /**
     * 同 getOrElse()，返回的是 Result容器
     *
     * @param s 缺省值生成函数
     * @return Result容器
     */
    default Result<T> orElse(Supplier<Result<T>> s) {
        return map(x -> this).getOrElse(s);
    }

    /**
     * 如果 Result 为 Success，将提供的映射函数应用于容器包含值，并将映射值用 Success 包含返回
     * 如果 Result 为 Success.EMPTY，则返回 Success.EMPTY
     * 如果 Result 为 Failure，返回一个failure实例
     *
     * @param <U>    映射目标类型
     * @param mapper 提供的映射函数
     * @return 如果映射成功，返回带映射数据的 Success，如果失败，返回 Failure
     */
    <U> Result<U> map(Function<? super T, ? extends U> mapper);

    /**
     * 如果 Result 为 Success，将提供的映射函数应用于容器包含值，并返回一个Result实例
     * 如果 Result 为 Success.EMPTY，则返回 Success.EMPTY
     * 如果 Result 为 Failure，返回一个failure实例
     *
     * @param <U>    映射目标类型
     * @param mapper 提供的映射函数
     * @return 如果映射成功，返回带映射数据的 Success，如果失败，返回 Failure
     */
    <U> Result<U> flatMap(Function<? super T, Result<U>> mapper);

    /**
     * 如果存在值，则返回true ，否则返回false
     *
     * @return 如果存在值，则返回true ，否则返回false
     */
    default boolean isPresent() {
        return get() != null;
    }

    /**
     * 如果 Result 为 Success，且存在值，则使用该值调用指定的使用者，否则什么也不做
     * 如果 Result 为 Failure，这个方法什么也不做
     *
     * @param action 处理函数
     */
    void ifPresent(Consumer<? super T> action);

    /**
     * 判断操作
     * 如果 Result 为 Success，断言成功时返回 true，如果断言失败，返回 false
     * 如果 Result 为 Failure，忽略断言，直接返回 false
     *
     * @param predicate 判断函数
     * @return 如上
     */
    boolean exists(Predicate<T> predicate);

    /**
     * 断言操作
     * 如果 Result 为 Success，断言成功时返回自身，如果断言失败，返回 Failure
     * 如果 Result 为 Failure，忽略断言，返回自身
     *
     * @param predicate 判断函数
     * @return 如上
     */
    Result<T> asserting(Predicate<? super T> predicate);

    /**
     * 断言操作
     * 如果 Result 为 Success，断言成功时返回自身，如果断言失败，返回 Failure
     * 如果 Result 为 Failure，忽略断言，返回自身
     *
     * @param predicate    判断函数
     * @param errorContext 错误上下文
     * @return 如上
     */
    Result<T> asserting(Predicate<? super T> predicate, ErrorContext errorContext);

    /**
     * 如果 Result 为 Success，这个方法什么也不做
     * 如果 Result 为 Failure，抛出错误
     */
    void orThrow();

    /**
     * 如果 Result 为 Success，这个方法什么也不做
     * 如果 Result 为 Failure，抛出错误
     *
     * @param errorContext 错误上下文
     * @apiNote {@code
     * Result.of(var).asserting(Object::nonNull).orThrow(VAR_IS_NULL_ERROR.as(" input data is null "));
     * }
     */
    void orThrow(ErrorContext errorContext);

    <V> V foldLeft(final V identity, Function<V, Function<T, V>> f);

    <V> V foldRight(final V identity, Function<T, Function<V, V>> f);

    /**
     * 工厂方法：返回 Success 实例
     *
     * @param value 成功结果
     * @param <T>   结果类型
     * @return Success 实例
     */
    static <T> Result<T> success(T value) {
        return ofNullable(value);
    }

    /**
     * 工厂方法： 返回 Success.EMPTY 实例
     *
     * @param <T> 预期成功结果的类型
     * @return Success.EMPTY 实例
     */
    static <T> Result<T> empty() {
        @SuppressWarnings("unchecked")
        Result<T> t = (Result<T>) Success.EMPTY;
        return t;
    }

    /**
     * 工厂方法：返回 Failure 实例
     *
     * @param errorContext 操作失败信息
     * @param <T>          预期成功结果的类型
     * @return Failure 实例
     */
    static <T> Result<T> failure(ErrorContext errorContext) {
        return new Failure<>(errorContext);
    }

    /**
     * 工厂方法：返回 Failure 实例
     * 操作失败实例转化
     *
     * @param failure 操作失败实例
     * @param <T>     预期成功结果的类型
     * @param <U>     预期成功结果的类型
     * @return Failure 实例
     */
    static <T, U> Result<T> failure(Failure<U> failure) {
        Objects.requireNonNull(failure, "When calling failure(Failure<U>) method, The input parameter is null");
        return new Failure<>(failure.errorContext);
    }

    /**
     * 工厂方法：返回 Failure 实例
     *
     * @param content       应答内容
     * @param reasonPattern 原因待格式化字符串
     * @param params        格式化参数（支持最后1个参数为Throwable）
     * @param <T>           预期成功结果的类型
     * @return Failure 实例
     */
    static <T> Result<T> failure(IResultEnum content, String reasonPattern, final Object... params) {
        Objects.requireNonNull(content, "When calling failure(IResultEnum, ...) method, The input parameter is null");
        return new Failure<>(content.as(reasonPattern, params));
    }

    /**
     * 工厂方法
     *
     * @param value 返回结果内容
     * @param <T>   返回结果类型
     * @return 返回 Result 实例
     */
    static <T> Result<T> of(final T value) {
        return of(value, ErrorContext.of(RESULT_CONTENT_ERROR));
    }

    /**
     * 工厂方法
     *
     * @param value 返回结果内容
     * @param <T>   返回结果类型
     * @return 返回 Result 实例
     */
    static <T> Result<T> ofNullable(final T value) {
        return value == null ? empty() : new Success<>(value);
    }

    /**
     * 工厂方法
     *
     * @param value        返回结果内容
     * @param errorContext 如果value为空，返回的错误上下文
     * @param <T>          返回结果类型
     * @return 返回 Result 实例
     */
    static <T> Result<T> of(final T value, final ErrorContext errorContext) {
        return of(Objects::nonNull, value, errorContext);
    }

    /**
     * 工厂方法
     *
     * @param predicate    判断函数
     * @param value        返回结果内容
     * @param errorContext 如果value为空，返回的错误上下文
     * @param <T>          返回结果类型
     * @return 返回 Result 实例
     */
    static <T> Result<T> of(final Predicate<T> predicate,
                            final T value,
                            final ErrorContext errorContext) {
        try {
            return predicate.test(value)
                    ? Result.success(value)
                    : Result.failure(errorContext);
        } catch (BusinessException e) {
            return Result.failure(e.getErrorContext());
        } catch (Exception e) {
            return Result.failure(errorContext.cause(e));
        }
    }

    /**
     * 工厂方法
     *
     * @param callable 返回结果的任务
     * @param <T>      返回结果的类型
     * @return 返回 Result 实例
     */
    static <T> Result<T> of(final Callable<T> callable) {
        return of(callable, ErrorContext.of(RESULT_CONTENT_ERROR));
    }

    /**
     * 工厂方法
     *
     * @param callable     返回结果的任务
     * @param errorContext 如果任务执行出错或执行结果为空，返回的错误上下文
     * @param <T>          返回结果的类型
     * @return 返回 Result 实例
     */
    static <T> Result<T> of(final Callable<T> callable, final ErrorContext errorContext) {
        try {
            T value = callable.call();
            return of(value, errorContext);
        } catch (BusinessException e) {
            return Result.failure(e.getErrorContext());
        } catch (Exception e) {
            return Result.failure(errorContext.cause(e));
        }
    }

    static <T> Result<T> flatten(Result<Result<T>> result) {
        return result.flatMap(x -> x);
    }

    static <A, B> Function<Result<A>, Result<B>> lift(final Function<A, B> f) {
        return x -> x.map(f);
    }

    static <A, B, C> Function<Result<A>, Function<Result<B>, Result<C>>> lift2(final Function<A, Function<B, C>> f) {
        return a -> b -> a.map(f).flatMap(b::map);
    }

    static <A, B, C, D> Function<Result<A>, Function<Result<B>, Function<Result<C>, Result<D>>>> lift3(final Function<A, Function<B, Function<C, D>>> f) {
        return a -> b -> c -> a.map(f).flatMap(b::map).flatMap(c::map);
    }

    static <A, B, C> Result<C> map2(final Result<A> a,
                                    final Result<B> b,
                                    final Function<A, Function<B, C>> f) {
        return lift2(f).apply(a).apply(b);
    }

    /**
     * 代表操作成功
     *
     * @param <T> 预期正常返回的数据类型
     * @author xiaohongchao
     * @since 1.0.0
     */
    class Success<T> implements Result<T> {

        private static final long serialVersionUID = 4534013032218150349L;

        /**
         * Common instance for empty()
         */
        private static final Result<?> EMPTY = new Success<>();

        private final T value;

        private Success() {
            super();
            this.value = null;
        }

        private Success(T value) {
            super();

            if (Objects.isNull(value)) {
                throw RESULT_CONSTRUCTION_ERROR.as("When Success instance is constructed, the instantiation parameter is null").toException();
            }

            this.value = Objects.requireNonNull(value);
        }

        @Override
        public Boolean isSuccess() {
            return true;
        }

        @Override
        public Boolean isFailure() {
            return false;
        }

        @Override
        public T successValue() {
            return value;
        }

        @Override
        public ErrorContext failureValue() {
            throw RESULT_INVOKE_ERROR.as("Method failureValue() called on a Success instance").toException();
        }

        @Override
        public T get() {
            return successValue();
        }

        @Override
        public void ifPresent(Consumer<? super T> action) {
            if (value != null) {
                action.accept(value);
            }
        }

        @Override
        public Result<T> asserting(Predicate<? super T> predicate) {
            return asserting(predicate, ErrorContext.of(RESULT_CONTENT_ERROR));
        }

        @Override
        public Result<T> asserting(Predicate<? super T> predicate, ErrorContext errorContext) {
            try {
                return predicate.test(successValue())
                        ? this
                        : failure(errorContext);
            } catch (BusinessException e) {
                return Result.failure(e.getErrorContext());
            } catch (Exception e) {
                return Result.failure(errorContext.cause(e));
            }
        }

        @Override
        public void orThrow() {
            /* Do nothing */
        }

        @Override
        public void orThrow(ErrorContext errorContext) {
            /* Do nothing */
        }

        @Override
        public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper);

            try {
                if (Objects.equals(this, EMPTY)) {
                    return empty();
                }

                return success(mapper.apply(successValue()));
            } catch (BusinessException e) {
                return Result.failure(e.getErrorContext());
            } catch (Exception e) {
                return Result.failure(ErrorContext.of(RESULT_MAP_ERROR).cause(e));
            }
        }

        @Override
        public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
            Objects.requireNonNull(mapper);
            try {
                if (Objects.equals(this, EMPTY)) {
                    return empty();
                }

                return mapper.apply(successValue());
            } catch (BusinessException e) {
                return Result.failure(e.getErrorContext());
            } catch (Exception e) {
                return Result.failure(ErrorContext.of(RESULT_MAP_ERROR).cause(e));
            }
        }

        @Override
        public boolean exists(Predicate<T> predicate) {
            return predicate.test(successValue());
        }

        @Override
        public <V> V foldLeft(V identity, Function<V, Function<T, V>> f) {
            return f.apply(identity).apply(successValue());
        }

        @Override
        public <V> V foldRight(V identity, Function<T, Function<V, V>> f) {
            return f.apply(successValue()).apply(identity);
        }
    }

    /**
     * 代表操作失败
     *
     * @param <T> 预期正常返回的数据类型
     * @author xiaohongchao
     * @since 1.0.0
     */
    class Failure<T> implements Result<T> {

        private static final long serialVersionUID = -1426284204330352085L;

        private final ErrorContext errorContext;

        private Failure(ErrorContext errorContext) {
            super();

            if (Objects.isNull(errorContext)) {
                throw RESULT_CONSTRUCTION_ERROR.as("When failure instance is constructed, the instantiation parameter is null").toException();
            }

            this.errorContext = Objects.requireNonNull(errorContext);
        }

        @Override
        public Boolean isSuccess() {
            return false;
        }

        @Override
        public Boolean isFailure() {
            return true;
        }

        @Override
        public T successValue() {
            throw RESULT_INVOKE_ERROR.as("Method successValue() called on a Failure instance").toException();
        }

        @Override
        public ErrorContext failureValue() {
            return this.errorContext;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public void ifPresent(Consumer<? super T> action) {
            /* Do nothing */
        }

        @Override
        public Result<T> asserting(Predicate<? super T> predicate) {
            return failure(this);
        }

        @Override
        public Result<T> asserting(Predicate<? super T> predicate, ErrorContext errorContext) {
            return failure(this);
        }

        @Override
        public void orThrow() {
            throw errorContext.toException();
        }

        @Override
        public void orThrow(ErrorContext errorContext) {
            throw errorContext.toException();
        }

        @Override
        public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
            return failure(this);
        }

        @Override
        public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
            return failure(this);
        }

        @Override
        public <V> V foldLeft(V identity, Function<V, Function<T, V>> f) {
            return identity;
        }

        @Override
        public <V> V foldRight(V identity, Function<T, Function<V, V>> f) {
            return identity;
        }

        @Override
        public boolean exists(Predicate<T> predicate) {
            return false;
        }
    }
}
