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

import static org.apache.commons.lang3.StringUtils.replace;

/**
 *
 */
public final class Strings {

    private Strings() {
    }

    public static final String SPACE = " ";
    public static final String CR = "\r";
    public static final String LF = "\n";

    /**
     * Replace carriage returns and linefeeds with a space
     */
    public static String flatten(String s) {
        return flatten(s, SPACE, SPACE);
    }

    /**
     * Replace carriage returns with <code>cr</code> and linefeeds with <code>lf</code>.
     */
    public static String flatten(String s, String cr, String lf) {
        return replace(replace(s, CR, cr), LF, lf);
    }

}
