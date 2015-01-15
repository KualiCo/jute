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

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

public class Loggers {

    /**
     * <p>
     * Convenience method for obtaining a logger
     * </p>
     *
     * <pre>
     * private static final Logger logger = Loggers.newLogger();
     * </pre>
     */
    public static Logger newLogger() {
        Throwable throwable = new Throwable();
        StackTraceElement[] elements = throwable.getStackTrace();
        StackTraceElement directCaller = elements[1];
        return getLogger(directCaller.getClassName());
    }

}
