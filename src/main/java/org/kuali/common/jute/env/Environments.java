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

import com.google.common.base.Predicate;

public final class Environments {

    private Environments() {}

    // Magic value used to indicate that a property value coming from the environment should be treated as though the property was not set.
    // This is useful when there are properties with a default value, that need to be "unset", for one reason or another.
    // This does mean that environments cannot contain a real property with the value ABSENT.
    public static final String ABSENT = "ABSENT";

    public static Predicate<CharSequence> absentPredicate() {
        return AbsentPredicate.INSTANCE;
    }

    private enum AbsentPredicate implements Predicate<CharSequence> {
        INSTANCE;

        @Override
        public boolean apply(CharSequence input) {
            return (input == null) || input.equals(ABSENT);
        }
    }

}
