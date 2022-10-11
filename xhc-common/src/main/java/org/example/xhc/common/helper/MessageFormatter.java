/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据非常简单的替换规则格式化消息。可以对 1 个、2 个或更多参数进行替换。
 * 例如，
 * MessageFormatter.format("Hi {}.", "there")
 * <p>
 * 将返回字符串“Hi there.”。
 * {} 对称为格式化锚。它用于指定需要在消息模式中替换参数的位置。
 * 如果您的消息包含“{”或“}”字符，则无需执行任何特殊操作，除非“}”字符紧跟在“{”之后。例如，
 * MessageFormatter.format("Set {1,2,3} is not equal to {}.", "1,2");
 * <p>
 * 将返回字符串“设置 {1,2,3} 不等于 1,2。”。
 * 如果出于某种原因您需要将字符串“{}”放入消息中而没有其格式化锚含义，那么您需要使用“\”转义“{”字符，即反斜杠字符。只有 '{' 字符应该被转义。无需转义 '}' 字符。例如，
 * MessageFormatter.format("Set \\{} is not equal to {}.", "1,2");
 * <p>
 * 将返回字符串“Set {} is not equal to 1,2.”。
 * 刚才描述的转义行为可以通过转义转义字符“\”来覆盖。打电话
 * MessageFormatter.format("File name is C:\\\\{}.", "file.zip");
 * <p>
 * 将返回字符串“ile name is C:\file.zip”。
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class MessageFormatter {
    private static final char DELIM_START = '{';
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    /**
     * 防止实例化
     */
    private MessageFormatter() {
    }

    /**
     * 对作为参数传递的“messagePattern”执行单个参数替换。
     * 例如，
     * MessageFormatter.format("Hi {}.", "there");
     * <p>
     * 将返回字符串“Hi there.”。
     *
     * @param messagePattern 将被解析和格式化的消息模式
     * @param arg            代替格式化锚的参数
     * @return 格式化的消息
     */
    public static FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    /**
     * 对作为参数传递的“messagePattern”执行两个参数替换。
     * 例如，
     * MessageFormatter.format("Hi {}. My name is {}.", "Alice", "Bob");
     * <p>
     * 将返回字符串“Hi Alice. My name is Bob.".
     *
     * @param messagePattern 将被解析和格式化的消息模式
     * @param arg1           代替第一个格式化锚点的参数
     * @param arg2           代替第二个格式化锚点的参数
     * @return 格式化的消息
     */
    public static FormattingTuple format(final String messagePattern, Object arg1, Object arg2) {
        return arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }


    /**
     * 对作为参数传递的“messagePattern”执行多参数替换。
     * 支持单独剥离最后1个参数为Throwable
     *
     * @param messagePattern 将被解析和格式化的消息模式
     * @param argArray       代替格式化锚的参数列表
     * @return 格式化的消息
     */
    public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = MessageFormatter.trimmedCopy(argArray);
        }
        return arrayFormat(messagePattern, args, throwableCandidate);
    }

    /**
     * 对作为参数传递的“messagePattern”执行多参数替换。
     * 支持单独剥离最后1个参数为Throwable
     *
     * @param messagePattern 将被解析和格式化的消息模式
     * @param argArray       代替格式化锚的参数列表
     * @param throwable      异常
     * @return 格式化的消息
     */
    public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray, Throwable throwable) {
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }

        if (argArray == null) {
            return new FormattingTuple(messagePattern, null, null);
        }

        int i = 0;
        int j;
        // use string builder for better multicore performance
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

        int L;
        for (L = 0; L < argArray.length; L++) {

            j = messagePattern.indexOf(DELIM_STR, i);

            if (j == -1) {
                // no more variables
                if (i == 0) {
                    // this is a simple string
                    return new FormattingTuple(messagePattern, argArray, throwable);
                } else {
                    // add the tail string which contains no variables and return the result.
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return new FormattingTuple(sbuf.toString(), argArray, throwable);
                }
            } else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        // DELIM_START was escaped, thus should not be incremented
                        L--;
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append(DELIM_START);
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        i = j + 2;
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                    i = j + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern, i, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    private static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) o);
            } else {
                objectArrayAppend(sbuf, (Object[]) o, seenMap);
            }
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
            report("Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void report(String msg, Throwable t) {
        System.err.println(msg);
        System.err.println("Reported exception:");
        t.printStackTrace();
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            // allow repeats in siblings
            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    /**
     * 确定Object数组是否包含Throwable作为最后一个元素的辅助方法
     *
     * @param argArray 我们想知道它是否包含Throwable作为最后一个元素的参数
     * @return 如果 argArray 中的最后一个Object是Throwable ，则此方法将返回它，否则返回 null
     */
    private static Throwable getThrowableCandidate(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }

        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }

        return null;
    }

    /**
     * 获取数组的最后一个元素以外的所有元素的辅助方法
     *
     * @param argArray 我们要从中删除最后一个元素的参数
     * @return 没有最后一个元素的数组副本
     */
    private static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("invalid empty or null argument array");
        }

        final int trimmedLen = argArray.length - 1;
        Object[] trimmed = new Object[trimmedLen];

        if (trimmedLen > 0) {
            System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
        }

        return trimmed;
    }

}
