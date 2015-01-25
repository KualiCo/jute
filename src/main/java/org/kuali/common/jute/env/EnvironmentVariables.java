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
package org.kuali.common.jute.env;

import static com.google.common.base.Ascii.isLowerCase;
import static com.google.common.base.Ascii.isUpperCase;
import static com.google.common.base.Functions.compose;
import static java.lang.Character.toLowerCase;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

public final class EnvironmentVariables {

    private EnvironmentVariables() {}

    public static ImmutableList<Function<String, String>> alternateKeyFunctions() {
        return ImmutableList.<Function<String, String>> copyOf(AlternateKeyFunctions.values());
    }

    private enum AlternateKeyFunctions implements Function<String, String> {
        // foo.barBaz -> FOO_BAR_BAZ
        CAMEL_CASE_UNDERSCORE_UPPER {
            @Override
            public String apply(String input) {
                return compose(UNDERSCORE, CAMEL_CASE_UPPER).apply(input);
            }
        },
        // foo.barBaz -> foo_barBaz
        UNDERSCORE {
            @Override
            public String apply(String input) {
                return input.replace('.', '_');
            }
        },
        // foo.barBaz -> FOO.BARBAZ
        UPPERCASE {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
            }
        },
        // foo.barBaz -> FOO_BARBAZ
        UNDERSCORE_UPPERCASE {
            @Override
            public String apply(String input) {
                return compose(UPPERCASE, UNDERSCORE).apply(input);
            }
        },
        // foo.barBaz -> foo.bar.baz
        CAMEL_CASE_LOWER {
            @Override
            public String apply(String input) {
                char[] chars = input.toCharArray();
                StringBuilder sb = new StringBuilder();
                char prevChar = 0;
                for (char c : chars) {
                    if (isUpperCase(c) && isLowerCase(prevChar)) {
                        // insert a dot on transitions from a lower case char to an upper case char
                        sb.append('.');
                        sb.append(toLowerCase(c));
                    } else {
                        // Just append the char
                        sb.append(c);
                    }
                    // Keep track of the previous char
                    prevChar = c;
                }
                return sb.toString();
            }
        },
        // foo.barBaz -> FOO.BAR.BAZ
        CAMEL_CASE_UPPER {
            @Override
            public String apply(String input) {
                return compose(UPPERCASE, CAMEL_CASE_LOWER).apply(input);
            }
        },
        // foo.barBaz -> foo_bar_baz
        CAMEL_CASE_UNDERSCORE_LOWER {
            @Override
            public String apply(String input) {
                return compose(UNDERSCORE, CAMEL_CASE_LOWER).apply(input);
            }
        },
    }

}
