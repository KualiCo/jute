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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 * Utility methods for creating exceptions with richly formatted error messages.
 *
 * <p>
 * Utility method for generating the stacktrace of an exception as a string.
 *
 * <p>
 * Example usage:
 *
 * <pre>
 * throw Exceptions.illegalArgument(&quot;port must be &gt;= %s and &lt;= %s&quot;, 0, 65535);
 * </pre>
 */
public final class Exceptions {

    private Exceptions() {
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static NullPointerException nullPointerException(String msg) {
        return new NullPointerException(msg);
    }

    public static NullPointerException nullPointerException(String msg, Object... args) {
        return new NullPointerException(format(msg, args));
    }

    public static IOException ioException(Throwable cause) {
        return new IOException(cause);
    }

    public static IOException ioException(String msg) {
        return new IOException(msg);
    }

    public static IOException ioException(String msg, Object... args) {
        return new IOException(format(msg, args));
    }

    public static IOException ioException(Throwable cause, String msg, Object... args) {
        return new IOException(format(msg, args), cause);
    }

    public static IllegalStateException illegalState(Throwable cause) {
        return new IllegalStateException(cause);
    }

    public static IllegalStateException illegalState(String msg) {
        return new IllegalStateException(msg);
    }

    public static IllegalStateException illegalState(String msg, Object... args) {
        return new IllegalStateException(format(msg, args));
    }

    public static IllegalStateException illegalState(Throwable cause, String msg, Object... args) {
        return new IllegalStateException(format(msg, args), cause);
    }

    public static IllegalArgumentException illegalArgument(Throwable cause) {
        return new IllegalArgumentException(cause);
    }

    public static IllegalArgumentException illegalArgument(String msg) {
        return new IllegalArgumentException(msg);
    }

    public static IllegalArgumentException illegalArgument(String msg, Object... args) {
        return new IllegalArgumentException(format(msg, args));
    }

    public static IllegalArgumentException illegalArgument(Throwable cause, String msg, Object... args) {
        return new IllegalArgumentException(format(msg, args), cause);
    }

    private static String format(String msg, Object... args) {
        return (args == null || args.length == 0) ? msg : String.format(msg, args);
    }

}
