/**
 * Copyright 2010-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.common.jute.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

/**
 * Strongly mimic's Guava's {@code Preconditions} class with a sensible default error message for common situations
 *
 * <pre>
 * {@code Guava:}
 * checkArgument(StringUtils.isNotBlank(foo), &quot;'foo' cannot be blank&quot;);
 * this.foo = foo;
 *
 * {@code Kuali:}
 * this.foo = checkNotBlank(foo, &quot;foo&quot;);
 * </pre>
 */
public class Precondition {

    private static final String NOT_NULL_MSG = "'%s' cannot be null";
    private static final String EXISTS_MSG = "[%s] does not exist";
    private static final String IS_DIR_MSG = "[%s] is not an existing directory";
    private static final String IS_FILE_MSG = "[%s] is not an existing file";
    private static final String NOT_BLANK_MSG = "'%s' cannot be blank";
    private static final String NOT_EMPTY_MSG = "'%s' cannot be the empty string";
    private static final String MIN_MSG = "%s not allowed. '%s' must be greater than or equal to %s";
    private static final String MAX_MSG = "%s not allowed. '%s' must be less than or equal to %s";
    private static final String EQUALS_MSG = "[%s] not allowed. '%s' must be equal to [%s]";
    private static final String NOT_EQUALS_MSG = "[%s] not allowed. '%s' must not be equal to [%s]";
    private static final String ARG_NAME = "argName";

    /**
     * Ensures that a File passed as an argument exists
     *
     * @param arg
     *            a File passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-null File that was validated
     *
     * @throws NullPointerException
     *             If arg is null. The exception message contains the name of the argument that was null
     * @throws IllegalArgumentException
     *             If arg does not exist or argName is blank
     */
    public static File checkExists(File arg, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkNotNull(arg, argName);
        checkArgument(arg.exists(), EXISTS_MSG, arg);
        return arg;
    }

    /**
     * Ensures that a File passed as an argument is an existing directory
     *
     * @param arg
     *            a File passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-null File that was validated
     *
     * @throws NullPointerException
     *             If arg is null. The exception message contains the name of the argument that was null
     * @throws IllegalArgumentException
     *             If arg does not exist, is not a directory, or argName is blank
     */
    public static File checkIsDir(File arg, String argName) {
        checkArgument(checkExists(arg, argName).isDirectory(), IS_DIR_MSG, arg);
        return arg;
    }

    /**
     * Ensures that a File passed as an argument is a normal file
     *
     * @param arg
     *            a File passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-null File that was validated
     *
     * @throws NullPointerException
     *             If arg is null. The exception message contains the name of the argument that was null
     * @throws IllegalArgumentException
     *             If arg does not exist, is not a normal file, or argName is blank
     */
    public static File checkIsFile(File arg, String argName) {
        checkArgument(checkExists(arg, argName).isFile(), IS_FILE_MSG, arg);
        return arg;
    }

    /**
     * Ensures that an object reference passed as an argument is not null
     *
     * @param arg
     *            an object reference passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-null object reference that was validated
     *
     * @throws NullPointerException
     *             If arg is null. The exception message contains the name of the argument that was null
     * @throws IllegalArgumentException
     *             If argName is blank
     */
    public static <T> T checkNotNull(T arg, String argName) {
        checkNotBlank(argName, ARG_NAME);
        return Preconditions.checkNotNull(arg, NOT_NULL_MSG, argName);
    }

    /**
     * Ensures that a String passed as an argument is not whitespace, empty ("") or null
     *
     * @param arg
     *            a String passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-blank String that was validated
     *
     * @throws IllegalArgumentException
     *             If arg is blank. The exception message contains the name of the argument that was blank
     * @throws IllegalArgumentException
     *             If argName is blank
     */
    public static String checkNotBlank(String arg, String argName) {
        checkArgument(isNotBlank(argName), NOT_BLANK_MSG, ARG_NAME);
        checkArgument(isNotBlank(arg), NOT_BLANK_MSG, argName);
        return arg;
    }

    /**
     * Ensures that a String passed as an argument is not the empty string ("") or null
     *
     * @param arg
     *            a String passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-blank String that was validated
     *
     * @throws IllegalArgumentException
     *             If arg is blank. The exception message contains the name of the argument that was blank
     * @throws IllegalArgumentException
     *             If argName is blank
     */
    public static String checkNotEmpty(String arg, String argName) {
        checkArgument(isNotBlank(argName), NOT_BLANK_MSG, ARG_NAME);
        checkArgument(isNotEmpty(arg), NOT_EMPTY_MSG, argName);
        return arg;
    }

    /**
     * Ensures that an {@code Optional<String>} passed as an argument does not contain a string that is whitespace or empty ("").
     *
     * @param arg
     *            an {@code Optional<String>} passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-blank {@code Optional<String>} that was validated
     *
     * @throws IllegalArgumentException
     *             If arg is blank. The exception message contains the name of the argument that was blank
     * @throws IllegalArgumentException
     *             If argName is blank
     */
    public static Optional<String> checkNotBlank(Optional<String> arg, String argName) {
        if (arg.isPresent()) {
            checkNotBlank(arg.get(), argName);
        }
        return arg;
    }

    /**
     * Ensures that an {@code Optional<String>} passed as an argument is not the empty string ("").
     *
     * @param arg
     *            an {@code Optional<String>} passed as an argument
     * @param argName
     *            the name of the argument
     *
     * @return the non-empty string {@code Optional<String>} that was validated
     *
     * @throws IllegalArgumentException
     *             If arg is blank. The exception message contains the name of the argument that was blank
     * @throws IllegalArgumentException
     *             If argName is blank
     */
    public static Optional<String> checkNotEmpty(Optional<String> arg, String argName) {
        if (arg.isPresent()) {
            checkArgument(isNotEmpty(arg.get()), NOT_EMPTY_MSG, argName);
        }
        return arg;
    }

    /**
     * If arg.isPresent(), check that the Integer it contains is greater than or equal to min
     */
    public static Optional<Integer> checkMin(Optional<Integer> arg, int min, String argName) {
        if (arg.isPresent()) {
            checkMin(arg.get(), min, argName);
        }
        return arg;
    }

    /**
     * If arg.isPresent(), check that the Long it contains is greater than or equal to min
     */
    public static Optional<Long> checkMin(Optional<Long> arg, long min, String argName) {
        if (arg.isPresent()) {
            checkMin(arg.get(), min, argName);
        }
        return arg;
    }

    /**
     * Check that arg is less than or equal to max
     */
    public static int checkMax(int arg, int max, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkArgument(arg <= max, MAX_MSG, arg, argName, max);
        return arg;
    }

    /**
     * Check that arg is less than or equal to max
     */
    public static long checkMax(long arg, long max, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkArgument(arg <= max, MAX_MSG, arg, argName, max);
        return arg;
    }

    public static int checkRange(int arg, int min, int max, String argName) {
        return checkMin(checkMax(arg, max, argName), min, argName);
    }

    /**
     * Check that arg is greater than or equal to min.
     */
    public static int checkMin(int arg, int min, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkArgument(arg >= min, MIN_MSG, arg, argName, min);
        return arg;
    }

    /**
     * Check that arg is greater than or equal to min.
     */
    public static long checkMin(long arg, long min, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkArgument(arg >= min, MIN_MSG, arg, argName, min);
        return arg;
    }

    public static <T> T checkNotEquals(T arg, T illegal, String argName) {
        checkNotNull(arg, checkNotBlank(argName, ARG_NAME));
        checkNotNull(illegal, "illegal");
        checkArgument(!arg.equals(illegal), NOT_EQUALS_MSG, argName, arg, illegal);
        return arg;
    }

    public static <T> T checkEquals(T arg, T expected, String argName) {
        checkNotNull(arg, checkNotBlank(argName, ARG_NAME));
        checkNotNull(expected, "expected");
        checkArgument(arg.equals(expected), EQUALS_MSG, argName, arg, expected);
        return arg;
    }

    public static long checkEquals(long arg, long expected, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkArgument(arg == expected, EQUALS_MSG, argName, arg, expected);
        return arg;
    }

    public static int checkEquals(int arg, int expected, String argName) {
        checkNotBlank(argName, ARG_NAME);
        checkArgument(arg == expected, EQUALS_MSG, argName, arg, expected);
        return arg;
    }

    public static void checkTruths(int expected, boolean... expressions) {
        checkMin(expected, 0, "expected");
        checkArgument(expressions.length > 0, "must supply at least 1 expression");
        int actual = 0;
        for (boolean expression : expressions) {
            actual = expression ? (actual + 1) : actual;
        }
        checkArgument(expected == actual, "expected %s of %s expression(s) to be true, but %s were true instead", expected, expressions.length, actual);
    }

    public static <I extends Iterable<T>, T> I checkSize(I iterable, int expected) {
        int actual = Iterables.size(iterable);
        checkArgument(expected == actual, "expected exactly %s element(s) but there were %s instead", expected, actual);
        return iterable;
    }

}
