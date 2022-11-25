/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.reply;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 表示可能会失败的操作结果
 * Result（表示数据或错误）
 * Success 子类型（表示操作成功、有数据）
 * Failure 子类型（表示操作失败、包含错误信息）
 * Empty 子类型（表示没有对应业务数据，即不是操作成功、也不是操作失败）
 *
 * @param <T> 预期正常返回的数据类型
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface Result<T> extends Serializable {

    Boolean isSuccess();

    Boolean isFailure();

    Boolean isEmpty();

    T getOrElse(final T defaultValue);

    T getOrElse(final Supplier<T> defaultValue);

    <V> V foldLeft(final V identity, Function<V, Function<T, V>> f);

    <V> V foldRight(final V identity, Function<T, Function<V, V>> f);

    T successValue();

    Exception failureValue();

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

    static <T, U> Result<T> failure(Failure<U> failure) {
        return new Failure<>(failure.exception);
    }

    static <T> Result<T> failure(String message) {
        return new Failure<>(message);
    }

    static <T> Result<T> failure(String message, Exception e) {
        return new Failure<>(new IllegalStateException(message, e));
    }

    static <V> Result<V> failure(Exception e) {
        return new Failure<>(e);
    }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> empty() {
        return new Empty<>();
    }

    static <T> Result<T> flatten(Result<Result<T>> result) {
        return result.flatMap(x -> x);
    }

    static <T> Result<T> of(final Callable<T> callable) {
        return of(callable, "Null value");
    }

    static <T> Result<T> of(final Callable<T> callable,
                            final String message) {
        try {
            T value = callable.call();
            return value == null
                    ? Result.failure(message)
                    : Result.success(value);
        } catch (Exception e) {
            return Result.failure(e.getMessage(), e);
        }
    }

    static <T> Result<T> of(final Predicate<T> predicate,
                            final T value,
                            final String message) {
        try {
            return predicate.test(value)
                    ? Result.success(value)
                    : Result.failure(String.format(message, value));
        } catch (Exception e) {
            String errMessage = String.format("Exception while evaluating predicate: %s", String.format(message, value));
            return Result.failure(errMessage, e);
        }
    }

    static <T> Result<T> of(final T value) {
        return value != null
                ? success(value)
                : Result.failure("Null value");
    }

    static <T> Result<T> of(final T value, final String message) {
        return value != null
                ? Result.success(value)
                : Result.failure(message);
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
        public T getOrElse(final T defaultValue) {
            return successValue();
        }

        @Override
        public T successValue() {
            return this.value;
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

        private final RuntimeException exception;

        private Failure(String message) {
            super();
            this.exception = new IllegalStateException(message);
        }

        private Failure(RuntimeException e) {
            super();
            this.exception = e;
        }

        private Failure(Exception e) {
            super();
            this.exception = new IllegalStateException(e);
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
        public RuntimeException failureValue() {
            return this.exception;
        }

        @Override
        public void forEachOrThrow(Consumer<T> c) {
            throw exception;
        }

        @Override
        public Result<RuntimeException> forEachOrException(Consumer<T> c) {
            return success(exception);
        }

        @Override
        public Result<String> forEachOrFail(Consumer<T> c) {
            return success(exception.getMessage());
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
            return failure(s, exception);
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
            return failure(exception.getMessage(), exception);
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
     * 代表操作成功
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
        public RuntimeException failureValue() {
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
