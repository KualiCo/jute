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
package org.kuali.common.jute.collect;

import static com.google.common.base.Preconditions.checkArgument;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Immutable version of <code>java.util.Properties</code> that is guaranteed to only contain string keys and values
 */
public final class ImmutableProperties extends Properties {

    private static final long serialVersionUID = 0;
    private static final String UOE_MSG = "Immutable properties cannot be changed";
    private static final Properties EMPTY = copyOf(new Properties());

    private ImmutableProperties(Properties mutable) {
        checkNotNull(mutable, "mutable");

        // Prevent anything from changing it until we are done
        synchronized (mutable) {

            // Extract only those keys where both the key and its corresponding value are strings
            Set<String> keys = mutable.stringPropertyNames();

            // If the sizes are different, it contains at least one key or value that is not a string
            checkArgument(keys.size() == mutable.size(), "Immutable properties only support strings");

            // Copy every key/value pair - can't use putAll() since it calls put() which is now disabled
            for (String key : keys) {
                super.put(key, mutable.getProperty(key));
            }
        }
    }

    public static Properties of(String name, String value) {
        Properties props = new Properties();
        props.setProperty(name, value);
        return copyOf(props);
    }

    public static Properties of() {
        return EMPTY;
    }

    /**
     * Create and return a new immutable properties object identical to the one passed in. If <code>properties</code> is already immutable, no new object is created, the
     * <code>properties</code> object passed in as a method argument is what is returned.
     *
     * @throws NullPointerException
     *             if {@code properties} is null
     */
    public static ImmutableProperties copyOf(Properties properties) {
        checkNotNull(properties, "properties");
        if (properties instanceof ImmutableProperties) {
            return (ImmutableProperties) properties;
        } else {
            return new ImmutableProperties(properties);
        }
    }

    public static ImmutableProperties copyOf(Map<String, String> map) {
        checkNotNull(map, "map");
        Properties properties = new Properties();
        for (Map.Entry<String, String> pair : map.entrySet()) {
            properties.setProperty(pair.getKey(), pair.getValue());
        }
        return copyOf(properties);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public Object setProperty(String key, String value) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public void load(Reader reader) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public void load(InputStream inStream) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public void loadFromXML(InputStream in) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public void putAll(Map<? extends Object, ? extends Object> t) {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    /**
     * @deprecated Not supported for immutable properties
     */
    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException(UOE_MSG);
    }

    @Override
    public Set<Object> keySet() {
        return ImmutableSet.copyOf(super.keySet());
    }

    @Override
    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        return ImmutableSet.copyOf(super.entrySet());
    }

    @Override
    public Collection<Object> values() {
        return ImmutableList.copyOf(super.values());
    }

}
