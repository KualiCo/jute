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

import com.google.common.collect.Range;

public final class Ports {

    private Ports() {
    }

    public static final int MIN = 0;
    public static final int MAX = 65535;
    public static final int SSH = 22;
    public static final int HTTP = 80;
    public static final int TOMCAT = 8080;
    public static final int HTTPS = 443;
    public static final int ORACLE = 1521;
    public static final int MYSQL = 3306;
    public static final int RDP = 3389;

    public static final Range<Integer> VALID_PORT_RANGE = Range.closed(MIN, MAX);

}
