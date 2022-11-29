/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.example.xhc.demo.base.common.IResultEnum;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.example.xhc.demo.base.common.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.example.xhc.demo.base.common.ErrorEnum.NULL_RESULT_ERROR;

/**
 * 服务调用结果
 * <p>
 * 表示可能会失败的操作，其操作结果为有数据或错误
 * <p>
 * 部分方法使用函数式编程概念
 * <p>
 * Success 子类型（表示操作成功、有数据）
 * Failure 子类型（表示操作失败、包含错误信息）
 * Empty 子类型（表示没有对应业务数据，即不是操作成功、也不是操作失败）
 *
 * @param <T> 预期正常返回的数据类型
 * @author xiaohongchao
 * @see java.util.Optional
 * @see java.util.List
 * @since 1.0.0
 */
public interface Result<T> extends Serializable {

    /**
     * 判断对象类型
     *
     * @return true -当前对象为 Success 子类型
     */
    Boolean isSuccess();

    /**
     * 判断对象类型
     *
     * @return true -当前对象为 Failure 子类型
     */
    Boolean isFailure();

    /**
     * 判断对象类型
     *
     * @return true -当前对象为 Empty 子类型
     */
    Boolean isEmpty();

    /**
     * 获取操作成功值
     *
     * @return 成功实例返回成功数据，失败实例返回异常
     */
    T successValue();

    ErrorContext failureValue();

    T getOrElse(final T defaultValue);

    T getOrElse(final Supplier<T> defaultValue);

    <V> V foldLeft(final V identity, Function<V, Function<T, V>> f);

    <V> V foldRight(final V identity, Function<T, Function<V, V>> f);

    void forEach(Consumer<T> c);

    void forEachOrThrow(Consumer<T> c);

    Result<String> forEachOrFail(Consumer<T> e);

    Result<RuntimeException> forEachOrException(Consumer<T> e);

    Result<T> filter(Predicate<T> p);

    Result<T> filter(Predicate<T> p, String message);

    <U> Result<U> map(Function<T, U> f);

    Result<T> mapFailure(String s, Exception e);

    Result<T> mapFailure(String s);

    Result<T> mapFailure(Exception e);

    Result<T> mapFailure(Result<T> v);

    Result<Nothing> mapEmpty();

    <U> Result<U> flatMap(Function<T, Result<U>> f);

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
        Objects.requireNonNull(failure);
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
        Objects.requireNonNull(content);
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
        return of(value, ErrorContext.of(NULL_RESULT_ERROR));
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
        return of(callable, ErrorContext.of(NULL_RESULT_ERROR));
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
        public T getOrElse(final T defaultValue) {
            return successValue();
        }

        @Override
        public RuntimeException failureValue() {
            throw new IllegalStateException("Method failureValue() called on a Success instance");
        }

        @Override
        public void forEach(Consumer<T> e) {
            e.accept(this.value);
        }

        @Override
        public void forEachOrThrow(Consumer<T> e) {
            e.accept(this.value);
        }

        @Override
        public Result<String> forEachOrFail(Consumer<T> e) {
            e.accept(this.value);
            return empty();
        }

        @Override
        public Result<RuntimeException> forEachOrException(Consumer<T> e) {
            e.accept(this.value);
            return empty();
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
        public T getOrElse(Supplier<T> defaultValue) {
            return successValue();
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
                    : INTERNAL_SERVER_ERROR.as("The cause of the error was not indicated");
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
        public T getOrElse(final T defaultValue) {
            return defaultValue;
        }

        @Override
        public T successValue() {
            throw new IllegalStateException("Method successValue() called on a Failure instance");
        }

        @Override
        public ErrorContext failureValue() {
            return this.errorContext;
        }

        @Override
        public void forEachOrThrow(Consumer<T> c) {
            throw errorContext.toException();
        }

        @Override
        public Result<RuntimeException> forEachOrException(Consumer<T> c) {
            return success(errorContext);
        }

        @Override
        public Result<String> forEachOrFail(Consumer<T> c) {
            return success(errorContext.getMessage());
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

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
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
        public T getOrElse(final T defaultValue) {
            return defaultValue;
        }

        @Override
        public T successValue() {
            throw new IllegalStateException("Method successValue() called on a Empty instance");
        }

        @Override
        public ErrorContext failureValue() {
            throw new IllegalStateException("Method failureMessage() called on a Empty instance");
        }

        @Override
        public void forEach(Consumer<T> c) {
            /* Empty. Do nothing. */
        }

        @Override
        public void forEachOrThrow(Consumer<T> c) {
            /* Do nothing */
        }

        @Override
        public Result<String> forEachOrFail(Consumer<T> c) {
            return empty();
        }

        @Override
        public Result<RuntimeException> forEachOrException(Consumer<T> c) {
            return empty();
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
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
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
