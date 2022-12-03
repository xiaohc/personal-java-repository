/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.example.xhc.demo.base.common.IResultEnum;
import org.example.xhc.demo.base.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ResultTest {
    @Test
    void testSuccess() {
        assertEquals("Value", Result.success("Value").get());
        assertTrue(Result.success(null) instanceof Result.Success);
    }

    @Test
    void testEmpty() {
        assertTrue(Result.empty() instanceof Result.Success);
    }

    @Test
    void testFailure() {
        IResultEnum iResultEnum = mock(IResultEnum.class);
        when(iResultEnum.as((String) any(), (Object[]) any())).thenReturn(ErrorContext.instance());
        assertTrue(Result.failure(iResultEnum, "Just cause", "Params") instanceof Result.Failure);
        verify(iResultEnum).as((String) any(), (Object[]) any());
    }

    @Test
    void testFailure2() {
        assertTrue(Result.failure(ErrorContext.instance()) instanceof Result.Failure);
    }

    @Test
    void testOf() {
        assertEquals("Value", Result.of("Value").get());
        assertTrue(Result.of((Object) null) instanceof Result.Success);
    }

    @Test
    void testOf2() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenReturn("Call");
        assertEquals("Call", Result.of(callable).get());
        verify(callable).call();
    }

    @Test
    void testOf3() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenThrow(new BusinessException(ErrorContext.instance()));
        assertTrue(Result.of(callable) instanceof Result.Failure);
        verify(callable).call();
    }

    @Test
    void testOf4() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenThrow(new Exception("foo"));
        assertTrue(Result.of(callable) instanceof Result.Failure);
        verify(callable).call();
    }

    @Test
    void testOf5() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenReturn(null);
        assertTrue(Result.of(callable) instanceof Result.Success);
        verify(callable).call();
    }

    @Test
    void testOf6() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenReturn("Call");
        assertEquals("Call", Result.of(callable, ErrorContext.instance()).get());
        verify(callable).call();
    }

    @Test
    void testOf7() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenThrow(new BusinessException(ErrorContext.instance()));
        assertTrue(Result.of(callable, ErrorContext.instance()) instanceof Result.Failure);
        verify(callable).call();
    }

    @Test
    void testOf8() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenThrow(new Exception("foo"));
        ErrorContext instanceResult = ErrorContext.instance();
        Result.of(callable, instanceResult);
        verify(callable).call();
    }

    @Test
    void testOf9() throws Exception {
        Callable<Object> callable = (Callable<Object>) mock(Callable.class);
        when(callable.call()).thenReturn(null);
        assertTrue(Result.of(callable, ErrorContext.instance()) instanceof Result.Success);
        verify(callable).call();
    }

    @Test
    void testOf10() {
        Predicate<Object> predicate = (Predicate<Object>) mock(Predicate.class);
        when(predicate.test((Object) any())).thenReturn(true);
        assertEquals("Value", Result.of(predicate, "Value", ErrorContext.instance()).get());
        verify(predicate).test((Object) any());
    }

    @Test
    void testOf11() {
        Predicate<Object> predicate = (Predicate<Object>) mock(Predicate.class);
        when(predicate.test((Object) any())).thenThrow(new BusinessException(ErrorContext.instance()));
        assertTrue(Result.of(predicate, "Value", ErrorContext.instance()) instanceof Result.Failure);
        verify(predicate).test((Object) any());
    }

    @Test
    void testOf12() {
        Predicate<Object> predicate = (Predicate<Object>) mock(Predicate.class);
        when(predicate.test((Object) any())).thenReturn(true);
        assertTrue(Result.of(predicate, null, ErrorContext.instance()) instanceof Result.Success);
        verify(predicate).test((Object) any());
    }

    @Test
    void testOf13() {
        Predicate<Object> predicate = (Predicate<Object>) mock(Predicate.class);
        when(predicate.test((Object) any())).thenReturn(false);
        assertTrue(Result.of(predicate, "Value", ErrorContext.instance()) instanceof Result.Failure);
        verify(predicate).test((Object) any());
    }

    @Test
    void testOf14() {
        Predicate<Object> predicate = (Predicate<Object>) mock(Predicate.class);
        when(predicate.test((Object) any())).thenReturn(false);
        assertTrue(Result.of(predicate, "Value", null) instanceof Result.Failure);
        verify(predicate).test((Object) any());
    }

    @Test
    void testLift2() {
        // TODO: This test is incomplete.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by lift2(Function)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        Result.lift2((Function<Object, Function<Object, Object>>) mock(Function.class)).apply(null);
    }

    @Test
    void testLift3() {
        // TODO: This test is incomplete.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by lift3(Function)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        Result.lift3((Function<Object, Function<Object, Function<Object, Object>>>) mock(Function.class)).apply(null);
    }
}

