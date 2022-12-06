/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.example.xhc.demo.base.common.IResultEnum;
import org.example.xhc.demo.base.exception.BusinessException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.example.xhc.demo.base.common.ErrorEnum.*;

/**
 * 容器类 : 表示可能会失败的操作，其操作结果为有数据或错误
 * <p>
 * Result.Success 子类型（表示操作成功）
 * Result.Failure 子类型（表示操作失败，包含错误信息）
 * Result.empty() 表示“无结果”，即数据缺失，对应可选数据 Option.empty()
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
     * 判断操作成功
     *
     * @return true -操作成功
     */
    boolean isSuccess();

    /**
     * 判断操作成功，且有操作结果
     * 如果存在值，则返回true ，否则返回false
     *
     * @return 如果存在值，则返回true ，否则返回false
     */
    default boolean isPresent() {
        return get() != null;
    }

    /**
     * 判断操作失败
     *
     * @return true - 操作失败
     */
    boolean isFailure();

    /**
     * 判断操作成功，但“无结果”
     *
     * @return true -操作成功，但“无结果”
     */
    default boolean isEmpty() {
        return Objects.equals(Success.EMPTY, this);
    }

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
        return map(v -> this).getOrElse(s);
    }

    /**
     * 如果 Result 为 Success，将提供的映射函数应用于容器包含值，并将映射值用 Success 包含返回
     * 如果 Result 为 Success.EMPTY，则返回 Success.EMPTY
     * 如果 Result 为 Failure，返回一个failure实例
     *
     * @param <U>    映射目标类型
     * @param mapper 提供的映射函数
     * @return 如果映射成功，返回带映射数据的 Success，如果失败，返回 Failure
     * @apiNote 复合如下三个方法
     * 函数复合: arg -> f3.apply(f2.apply(f1.apply(arg)))
     * {@code
     * Integer function1(String str);
     * Long function2(Integer str);
     * String function3(Long str);
     * <p>
     * Result<String> result = Result.of("test").map(Class1::function1).Map(Class2::function2).Map(Class3::function3)
     * }
     * 注意：一般来说，缺失数据是错误的情况，但上面处理会直接返回Result.Success.empty()而没有抛出异常，类似捕获了异常并吞掉了。
     * 所有如果有对应要求，需要各个function自己抛出异常，或在中间穿插asserting()检查
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
     * @apiNote 复合如下三个方法
     * 1、函数复合: arg -> f3.apply(f2.apply(f1.apply(arg)))
     * {@code
     * Result<String> function1(String str);
     * Result<String> function2(String str);
     * Result<String> function3(String str);
     * <p>
     * Result<String> result = Object1.function1("test").flatMap(Class2::function2).flatMap(Class3::function3)
     * }
     * 注意：一般来说，缺失数据是错误的情况，但上面处理会直接返回Result.Success.empty()而没有抛出异常，类似捕获了异常并吞掉了。
     * 所有如果有对应要求，需要各个function自己抛出异常，或在中间穿插asserting()检查
     * 2. 解析式模式
     * {@code
     * private int compute(int p1, int p2, int p3, int p4, int p5) {  return p1 + p2 + p3 + p4 + p5;  }
     * <p>
     * Result<Integer> result1 = success(1);
     * Result<Integer> result2 = success(2);
     * Result<Integer> result3 = success(3);
     * Result<Integer> result4 = success(4);
     * Result<Integer> result5 = success(5);
     * <p>
     * Result<Integer> result = result1.flatMap(p1 -> result2
     * .flatMap(p2 -> result3
     * .flatMap(p3 -> result4
     * .flatMap(p4 -> result5
     * .map(p5 -> compute(p1, p2, p3, p4, p5))))));
     * <p>
     * assertThat(result).isNotNull().isInstanceOf(Result.Success.class);
     * assertThat(result.get()).isEqualTo(15);
     * }
     */
    <U> Result<U> flatMap(Function<? super T, Result<U>> mapper);

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
     * 如果 Result 为 Success，这个方法直接返回自身
     * 如果 Result 为 Failure，抛出错误
     *
     * @return 返回自身
     */
    Result<T> orThrow();

    /**
     * 如果 Result 为 Success，这个方法直接返回自身
     * 如果 Result 为 Failure，抛出错误
     *
     * @param errorContext 错误上下文
     * @return 返回自身
     * @apiNote {@code
     * Result.of(var).asserting(Object::nonNull).orThrow(VAR_IS_NULL_ERROR.as(" input data is null "));
     * }
     */
    Result<T> orThrow(ErrorContext errorContext);

    /**
     * 工厂方法：返回 Success 实例
     *
     * @param value 成功结果
     * @param <T>   结果类型
     * @return Success 实例
     */
    static <T> Result<T> success(T value) {
        return value == null ? empty() : new Success<>(value);
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
     * empty实例转化为 Failure实例
     *
     * @param errorContext 操作失败信息
     * @return Failure 实例
     */
    default Result<T> emptyToFailure(ErrorContext errorContext) {
        return isEmpty() ? failure(errorContext) : this;
    }

    /**
     * 工厂方法：返回 Failure 实例
     *
     * @param errorContext 操作失败信息
     * @param <T>          预期成功结果的类型
     * @return Failure 实例
     */
    static <T> Result<T> failure(ErrorContext errorContext) {
        Objects.requireNonNull(errorContext, "When calling failure(ErrorContext) method, The input parameter is null");
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
     * @return 返回 Success 实例
     */
    static <T> Result<T> of(final T value) {
        return success(value);
    }

    /**
     * 工厂方法
     *
     * @param predicate    判断函数
     * @param value        返回结果内容
     * @param errorContext 如果value为空，返回的错误上下文
     * @param <T>          返回结果类型
     * @return 返回 Result 实例
     * @apiNote {@code
     * of(Objects::nonNull, value, errorContext);
     * }
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
            final ErrorContext error = Objects.nonNull(errorContext) ? errorContext : ErrorContext.of(RESULT_CREATION_ERROR);
            return Result.failure(error.cause(e));
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
        return of(callable, null);
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
            return of(value);
        } catch (BusinessException e) {
            return Result.failure(e.getErrorContext());
        } catch (Exception e) {
            final ErrorContext error = Objects.nonNull(errorContext) ? errorContext : ErrorContext.of(RESULT_CREATION_ERROR);
            return Result.failure(error.cause(e));
        }
    }

    /**
     * 将从 A 到 B 的函数转换为从 Result<A> 到 Result<B> 的函数
     *
     * @param f   从 A 到 B 的函数
     * @param <A> 源类型
     * @param <B> 目标类型
     * @return 从 Result<A> 到 Result<B> 的函数
     */
    static <A, B> Function<Result<A>, Result<B>> lift(final Function<A, B> f) {
        return v -> v.map(f);
    }

    /**
     * 提取 Result<Result<T>> 包含的 Result<T> 内容
     *
     * @param result Result<Result<T>>
     * @param <T>    操作结果的类型
     * @return 返回 Result<T>
     */
    static <T> Result<T> flatten(Result<Result<T>> result) {
        Objects.requireNonNull(result);
        return result.flatMap(v -> v);
    }

    /**
     * 提取 List<Result<T>> 中值到新列表，其包含原始列表中所有值为 Success 的元素， 并忽略 Failure 和 Empty
     * emp ty 值。
     *
     * @param list 原始列表
     * @param <T>  操作结果的类型
     * @return 返回  List<A>
     */
    static <T> List<T> flattenList(List<Result<T>> list) {
        Objects.requireNonNull(list);
        return list.stream().map(Result::get).filter(Objects::nonNull).collect(Collectors.toList());
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
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
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
            return asserting(predicate, null);
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
                final ErrorContext error = Objects.nonNull(errorContext) ? errorContext : ErrorContext.of(RESULT_CONTENT_ERROR);
                return Result.failure(error.cause(e));
            }
        }

        @Override
        public Result<T> orThrow() {
            return this;
        }

        @Override
        public Result<T> orThrow(ErrorContext errorContext) {
            return this;
        }

        @Override
        public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper);

            try {
                if (isEmpty()) {
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
                if (isEmpty()) {
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
            this.errorContext = Objects.requireNonNull(errorContext);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
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
        public Result<T> orThrow() {
            throw errorContext.toException();
        }

        @Override
        public Result<T> orThrow(ErrorContext errorContext) {
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
        public boolean exists(Predicate<T> predicate) {
            return false;
        }
    }
}
