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
package org.kuali.common.jute.reflect;

import static com.google.common.collect.Maps.newConcurrentMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Exceptions.nullPointerException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public final class Reflection {

    private Reflection() {}

    private static final Map<Class<?>, ImmutableSet<Field>> CACHE = newConcurrentMap();

    public static <T> T checkNoNulls(T instance) {
        for (Field field : getFields(instance.getClass())) {
            if (getValue(field, instance) == null) {
                throw nullPointerException("%s.%s cannot be null", instance.getClass().getSimpleName(), field.getName());
            }
        }
        return instance;
    }

    /**
     * <p>
     * Recursively examine the type hierarchy and extract all fields encountered anywhere in the hierarchy into an immutable set
     * </p>
     *
     * <p>
     * NOTE: field.getName() is not necessarily unique for the elements in the set
     * </p>
     */
    private static ImmutableSet<Field> getFields(Class<?> type) {
        ImmutableSet<Field> fields = CACHE.get(type);
        if (fields == null) {
            Set<Field> mutable = newHashSet();
            for (Class<?> c = type; c != null; c = c.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    mutable.add(field);
                }
            }
            fields = ImmutableSet.copyOf(mutable);
            CACHE.put(type, fields);
        }
        return fields;
    }

    /**
     * Unconditionally attempt to get the value of this field on this instance.
     */
    private static <T> Object getValue(Field field, T instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw illegalState(e);
        }
    }
}
