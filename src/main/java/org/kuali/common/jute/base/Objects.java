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

import static com.google.common.base.Preconditions.checkNotNull;

public class Objects {

    /**
     * Return true if {@code reference} and {@code object} are not null, the exact same runtime type, and {@code reference.compareTo(object) == 0}
     *
     * @throws NullPointerException
     *             If {@code reference} is {@code null}
     */
    public static <T extends Comparable<? super T>> boolean equalByComparison(T reference, Object object) {
        if (notEqual(reference, object)) {
            return false;
        } else {
            // notEqual() guarantees that reference and object are the exact same runtime type
            @SuppressWarnings("unchecked")
            T other = (T) object;
            return reference.compareTo(other) == 0;
        }

    }

    /**
     * <p>
     * Return true if <code>reference</code> is definitely not equal to <code>other</code>.
     * </p>
     *
     * <p>
     * If <code>other</code> is null <b>OR</b> a different runtime type than <code>reference</code>, return true
     * </p>
     *
     * @param reference
     *            The object <code>other</code> is being compared to.
     * @param other
     *            The object being examined for equality with <code>reference</code>.
     *
     * @throws NullPointerException
     *             If <code>reference</cod> is <code>null</code>
     */
    public static boolean notEqual(Object reference, Object other) {
        checkNotNull(reference);
        if (other == null) {
            return true;
        } else {
            return !reference.getClass().equals(other.getClass());
        }
    }

}
