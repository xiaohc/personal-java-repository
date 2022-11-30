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
     * @param s 缺省值生成函数
     * @return 容器存储数据（value）
     */
    T getOrElse(final Supplier<T> s);

    /**
     * Result为操作成功时，循环处理 Result容器内数据，
     * Result为其他情况时，不做任何处理，直接处理成功
     *
     * @param action 处理函数
     */
    void forEach(Consumer<? super T> action);

    /**
     * Result为操作成功时，循环处理 Result容器内数据
     * Result为操作失败时，直接抛出异常
     * Result为其他情况时，不做任何处理，直接处理成功
     *
     * @param action 处理函数
     */
    void forEachOrThrow(Consumer<? super T> action);

    /**
     * 断言操作
     *
     * @param predicate 判断函数
     * @return 如下：
     * Result为操作成功时，断言成功时返回自身，如果断言失败，返回 Result.failure操作失败
     * Result为其他情况时，忽略断言，不做任何处理，返回自身
     */
    Result<T> asserting(Predicate<? super T> predicate);

    /**
     * 断言操作
     *
     * @param predicate    判断函数
     * @param errorContext 错误上下文
     * @return 如下：
     * Result为操作成功时，断言成功时返回自身，如果断言失败，返回 Result.failure操作失败
     * Result为其他情况时，忽略断言，不做任何处理，返回自身
     */
    Result<T> asserting(Predicate<? super T> predicate, ErrorContext errorContext);

    /**
     * 如果 Result 为 Failure，抛出错误
     * 如果 Result 为 Empty 或 Success，这个方法什么也不做
     */
    void orThrow();

    /**
     * 如果 Result 为 Failure，抛出错误
     * 如果 Result 为 Empty 或 Success，这个方法什么也不做
     *
     * @param errorContext 错误上下文
     * @apiNote {@code
     * Result.of(var).asserting(Object::nonNull).thenThrow(VAR_IS_NULL_ERROR.as(" input data is null "));
     * }
     */
    void orThrow(ErrorContext errorContext);

    /**
     * Result为操作成功时，将提供的映射函数应用于容器包含值，并将映射值用 Result实例包含返回
     * Result为操作失败时，返回一个failure实例
     * Result为其他情况时，返回一个empty实例
     *
     * @param <U>    映射目标类型
     * @param mapper 提供的映射函数
     * @return 如果映射成功，Result实例值为映射数据，如果失败，Result实例为failure实例
     */
    <U> Result<U> map(Function<? super T, U> mapper);

    /**
     * Result为操作成功时，将提供的映射函数应用于容器包含值，并返回一个Result实例
     * Result为操作失败时，返回一个failure实例
     * Result为其他情况时，返回一个empty实例
     *
     * @param <U>    映射目标类型
     * @param mapper 提供的映射函数
     * @return 如果映射成功，Result实例值为映射数据，如果失败，Result实例为failure实例
     */
    <U> Result<U> flatMap(Function<? super T, Result<U>> mapper);

    Result<T> mapFailure(String s, Exception e);

    Result<T> mapFailure(String s);

    Result<T> mapFailure(Exception e);

    Result<T> mapFailure(Result<T> v);

    Result<Nothing> mapEmpty();

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
        @SuppressWarnings("unchecked")
        Result<T> t = (Result<T>) Empty.INSTANCE;
        return t;
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
        return of(value, ErrorContext.of(RESULT_CONTENT_ERROR));
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
        return of(callable, ErrorContext.of(RESULT_CONTENT_ERROR));
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

        /**
         * Common instance for empty()
         */
        public static final Result<?> EMPTY = success(null);

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
        public T getOrElse(Supplier<T> s) {
            return successValue();
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            action.accept(this.value);
        }

        @Override
        public void forEachOrThrow(Consumer<? super T> action) {
            action.accept(this.value);
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
        public <U> Result<U> map(Function<? super T, U> mapper) {
            try {
                return success(mapper.apply(successValue()));
            } catch (BusinessException e) {
                return Result.failure(e.getErrorContext());
            } catch (Exception e) {
                return Result.failure(ErrorContext.of(RESULT_MAP_ERROR).cause(e));
            }
        }

        @Override
        public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
            try {
                return mapper.apply(successValue());
            } catch (BusinessException e) {
                return Result.failure(e.getErrorContext());
            } catch (Exception e) {
                return Result.failure(ErrorContext.of(RESULT_MAP_ERROR).cause(e));
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

            if (Objects.isNull(errorContext)) {
                throw RESULT_CONSTRUCTION_ERROR.as("When failure instance is constructed, the instantiation parameter is null").toException();
            }

            this.errorContext = errorContext;
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
        public T getOrElse(Supplier<T> s) {
            Objects.requireNonNull(s);
            return s.get();
        }

        @Override
        public void forEachOrThrow(Consumer<? super T> action) {
            throw errorContext.toException();
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
        public <U> Result<U> map(Function<? super T, U> mapper) {
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
        public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
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

        private static final Result<?> INSTANCE = new Empty<>();

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
        public T getOrElse(Supplier<T> s) {
            Objects.requireNonNull(s);
            return s.get();
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            /* Empty. Do nothing. */
        }

        @Override
        public void forEachOrThrow(Consumer<? super T> action) {
            /* Do nothing */
        }

        @Override
        public Result<T> asserting(Predicate<? super T> predicate) {
            return empty();
        }

        @Override
        public Result<T> asserting(Predicate<? super T> predicate, ErrorContext errorContext) {
            return empty();
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
        public <U> Result<U> map(Function<? super T, U> mapper) {
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
        public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
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
