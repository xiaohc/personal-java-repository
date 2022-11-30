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
 * Success 子类型（表示操作成功、有数据）
 * Failure 子类型（表示操作失败、包含错误信息）
 * Empty 子类型（表示操作无效，即不是操作成功、也不是操作失败，也表示没有对应业务数据）
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
     * Common instance for empty()
     */
    Result<?> EMPTY = success(null);

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
     * 判断操作结果
     *
     * @return true -操作无效，也表示没有对应业务数据
     */
    Boolean isEmpty();

    /**
     * Result为操作成功时，返回 Result容器存储的数据（value）
     * Result为其他情况时，抛出异常
     * <p>
     * 同： T get()
     *
     * @return 容器存储数据（value）
     */
    T successValue();

    /**
     * Result为操作失败时，返回 返回错误上下文
     * Result为其他情况时，抛出异常
     *
     * @return 错误上下文
     */
    ErrorContext failureValue();

    /**
     * Result为操作成功时，返回 Result容器存储的数据（value）
     * Result为其他情况时，返回缺省值
     *
     * @param defaultValue 缺省值
     * @return 容器存储数据（value）
     */
    T getOrElse(final T defaultValue);

    /**
     * Result为操作成功时，返回 Result容器存储的数据（value）
     * Result为其他情况时，返回缺省值
     *
     * @param defaultValue 缺省值函数
     * @return 容器存储数据（value）
     */
    T getOrElse(final Supplier<T> defaultValue);

    /**
     * Result为操作成功时，循环处理 Result容器内数据，
     * Result为其他情况时，不做任何处理，直接处理成功
     *
     * @param consumer 处理函数
     */
    void forEach(Consumer<T> consumer);

    /**
     * Result为操作成功时，循环处理 Result容器内数据
     * Result为操作失败时，直接抛出异常
     * Result为其他情况时，不做任何处理，直接处理成功
     *
     * @param consumer 处理函数
     */
    void forEachOrThrow(Consumer<T> consumer);

    Result<T> filter(Predicate<T> p);

    Result<T> filter(Predicate<T> p, String message);

    <U> Result<U> map(Function<T, U> f);

    Result<T> mapFailure(String s, Exception e);

    Result<T> mapFailure(String s);

    Result<T> mapFailure(Exception e);

    Result<T> mapFailure(Result<T> v);

    Result<Nothing> mapEmpty();

    <U> Result<U> flatMap(Function<T, Result<U>> f);

    <V> V foldLeft(final V identity, Function<V, Function<T, V>> f);

    <V> V foldRight(final V identity, Function<T, Function<V, V>> f);

    Boolean exists(Predicate<T> p);

    default Result<T> orElse(Supplier<Result<T>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    /**
     * 工厂方法：返回操作成功实例
     *
     * @param value 成功结果
     * @param <T>   结果类型
     * @return 操作成功实例
     */
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * 工厂方法： 返回操作无效实例
     *
     * @param <T> 预期成功结果的类型
     * @return 操作无效实例
     */
    static <T> Result<T> empty() {
        return new Empty<>();
    }

    /**
     * 工厂方法：返回操作失败实例
     *
     * @param errorContext 操作失败信息
     * @param <T>          预期成功结果的类型
     * @return 操作失败实例
     */
    static <T> Result<T> failure(ErrorContext errorContext) {
        return new Failure<>(errorContext);
    }

    /**
     * 工厂方法：返回操作失败实例
     * 操作失败实例转化
     *
     * @param failure 操作失败实例
     * @param <T>     预期成功结果的类型
     * @param <U>     预期成功结果的类型
     * @return 操作失败实例
     */
    static <T, U> Result<T> failure(Failure<U> failure) {
        Objects.requireNonNull(failure, "When calling failure(Failure<U>) method, The input parameter is null");
        return new Failure<>(failure.errorContext);
    }

    /**
     * 工厂方法：返回操作失败实例
     *
     * @param content       应答内容
     * @param reasonPattern 原因待格式化字符串
     * @param params        格式化参数（支持最后1个参数为Throwable）
     * @param <T>           预期成功结果的类型
     * @return 操作失败实例
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
     * @return 返回操作结果实例
     */
    static <T> Result<T> of(final T value) {
        return of(value, ErrorContext.of(RESULT_NULL_VALUE_ERROR));
    }

    /**
     * 工厂方法
     *
     * @param value        返回结果内容
     * @param errorContext 如果value为空，返回的错误上下文
     * @param <T>          返回结果类型
     * @return 返回操作结果实例
     */
    static <T> Result<T> of(final T value, final ErrorContext errorContext) {
        return of(Objects::nonNull, value, errorContext);
    }

    /**
     * 工厂方法
     *
     * @param callable 返回结果的任务
     * @param <T>      返回结果的类型
     * @return 返回操作结果实例
     */
    static <T> Result<T> of(final Callable<T> callable) {
        return of(callable, ErrorContext.of(RESULT_NULL_VALUE_ERROR));
    }

    /**
     * 工厂方法
     *
     * @param callable     返回结果的任务
     * @param errorContext 如果任务执行出错或执行结果为空，返回的错误上下文
     * @param <T>          返回结果的类型
     * @return 返回操作结果实例
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

    /**
     * 工厂方法
     *
     * @param predicate    判断函数
     * @param value        返回结果内容
     * @param errorContext 如果value为空，返回的错误上下文
     * @param <T>          返回结果类型
     * @return 返回操作结果实例
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

        private final T value;

        private Success(T value) {
            super();
            this.value = value;
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
        public Boolean isEmpty() {
            return false;
        }

        @Override
        public T successValue() {
            return this.value;
        }

        @Override
        public ErrorContext failureValue() {
            throw RESULT_INVOKE_ERROR.as("Method failureValue() called on a Success instance").toException();
        }

        @Override
        public T getOrElse(final T defaultValue) {
            return successValue();
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return successValue();
        }

        @Override
        public void forEach(Consumer<T> consumer) {
            consumer.accept(this.value);
        }

        @Override
        public void forEachOrThrow(Consumer<T> consumer) {
            consumer.accept(this.value);
        }

        @Override
        public Result<T> filter(Predicate<T> p) {
            return filter(p, "Unmatched predicate with no error message provided.");
        }

        @Override
        public Result<T> filter(Predicate<T> p, String message) {
            try {
                return p.test(successValue())
                        ? this
                        : failure(message);
            } catch (Exception e) {
                return failure(e.getMessage(), e);
            }
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            try {
                return success(f.apply(successValue()));
            } catch (Exception e) {
                return failure(e.getMessage(), e);
            }
        }

        @Override
        public Result<T> mapFailure(String f, Exception e) {
            return this;
        }

        @Override
        public Result<T> mapFailure(String s) {
            return this;
        }

        @Override
        public Result<T> mapFailure(Exception e) {
            return this;
        }

        @Override
        public Result<T> mapFailure(Result<T> v) {
            return this;
        }

        @Override
        public Result<Nothing> mapEmpty() {
            return failure("Not empty");
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            try {
                return f.apply(successValue());
            } catch (Exception e) {
                return failure(e.getMessage());
            }
        }

        @Override
        public String toString() {
            return String.format("Success(%s)", successValue().toString());
        }

        @Override
        public Boolean exists(Predicate<T> p) {
            return p.test(successValue());
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
    class Failure<T> extends Empty<T> {

        private static final long serialVersionUID = -1426284204330352085L;

        private final ErrorContext errorContext;

        private Failure(ErrorContext errorContext) {
            super();
            this.errorContext = errorContext != null
                    ? errorContext
                    : RESULT_CONSTRUCTION_ERROR.as("When failure instance is constructed, the instantiation parameter is null");
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
        public T getOrElse(final T defaultValue) {
            return defaultValue;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public void forEachOrThrow(Consumer<T> consumer) {
            throw errorContext.toException();
        }

        @Override
        public Result<T> filter(Predicate<T> p) {
            return failure(this);
        }

        @Override
        public Result<T> filter(Predicate<T> p, String message) {
            return failure(this);
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return failure(this);
        }

        @Override
        public Result<T> mapFailure(String s, Exception e) {
            return failure(s, e);
        }

        @Override
        public Result<T> mapFailure(String s) {
            return failure(s, errorContext);
        }

        @Override
        public Result<T> mapFailure(Exception e) {
            return failure(e.getMessage(), e);
        }

        @Override
        public Result<T> mapFailure(Result<T> v) {
            return v;
        }

        @Override
        public Result<Nothing> mapEmpty() {
            return failure(this);
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return failure(errorContext.getMessage(), errorContext);
        }

        @Override
        public String toString() {
            return String.format("Failure(%s)", failureValue());
        }

        @Override
        public Boolean exists(Predicate<T> p) {
            return false;
        }


    }

    /**
     * 表示没有对应业务数据，即不是操作成功、也不是操作失败
     *
     * @param <T> 预期正常返回的数据类型
     * @author xiaohongchao
     * @since 1.0.0
     */
    class Empty<T> implements Result<T> {

        private static final long serialVersionUID = 3499319831519197581L;

        private Empty() {
            super();
        }

        @Override
        public Boolean isSuccess() {
            return false;
        }

        @Override
        public Boolean isFailure() {
            return false;
        }

        @Override
        public Boolean isEmpty() {
            return true;
        }

        @Override
        public T successValue() {
            throw RESULT_INVOKE_ERROR.as("Method successValue() called on a Empty instance").toException();
        }

        @Override
        public ErrorContext failureValue() {
            throw RESULT_INVOKE_ERROR.as("Method failureValue() called on a Empty instance").toException();
        }

        @Override
        public T getOrElse(final T defaultValue) {
            return defaultValue;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public void forEach(Consumer<T> consumer) {
            /* Empty. Do nothing. */
        }

        @Override
        public void forEachOrThrow(Consumer<T> consumer) {
            /* Do nothing */
        }

        @Override
        public Result<T> filter(Predicate<T> p) {
            return empty();
        }

        @Override
        public Result<T> filter(Predicate<T> p, String message) {
            return empty();
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return empty();
        }

        @Override
        public Result<T> mapFailure(String s, Exception e) {
            return failure(s, e);
        }

        @Override
        public Result<T> mapFailure(String s) {
            return failure(s);
        }

        @Override
        public Result<T> mapFailure(Exception e) {
            return failure(e.getMessage(), e);
        }

        @Override
        public Result<T> mapFailure(Result<T> v) {
            return v;
        }

        @Override
        public Result<Nothing> mapEmpty() {
            return success(Nothing.INSTANCE);
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return empty();
        }

        @Override
        public String toString() {
            return "Empty()";
        }

        @Override
        public Boolean exists(Predicate<T> p) {
            return false;
        }

        @Override
        public <V> V foldLeft(V identity, Function<V, Function<T, V>> f) {
            return identity;
        }

        @Override
        public <V> V foldRight(V identity, Function<T, Function<V, V>> f) {
            return identity;
        }
    }
}
