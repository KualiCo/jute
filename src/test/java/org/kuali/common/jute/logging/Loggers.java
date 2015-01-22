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
package org.kuali.common.jute.logging;

import static java.util.logging.Level.ALL;
import static java.util.logging.Logger.getLogger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Loggers {

    private static final Handler HANDLER = getHandler();

    public static Logger newLogger() {
        Throwable throwable = new Throwable();
        StackTraceElement[] elements = throwable.getStackTrace();
        StackTraceElement directCaller = elements[1];
        return getLogger(directCaller.getClassName());
    }

    public static <T> Logger newLogger(T instance) {
        return newLogger(instance.getClass());
    }

    public static <T> Logger newLogger(Class<T> type) {
        return newLogger(type.getName());
    }

    public static Logger newLogger(String name) {
        Logger logger = getLogger(name);
        logger.setUseParentHandlers(false);
        logger.addHandler(HANDLER);
        return logger;
    }

    private static Handler getHandler() {
        Handler handler = new ConsoleHandler();
        handler.setLevel(ALL);
        handler.setFormatter(new LogFormatter());
        return handler;
    }

}
