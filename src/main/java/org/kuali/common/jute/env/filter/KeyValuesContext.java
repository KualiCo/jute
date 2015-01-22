package org.kuali.common.jute.env.filter;

import static java.util.Collections.singletonList;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

public final class KeyValuesContext {

    private final String key;
    private final ImmutableList<String> values;

    private KeyValuesContext(Builder builder) {
        this.key = builder.key;
        this.values = ImmutableList.copyOf(builder.values);
    }

    public static KeyValuesContext build(String key) {
        return build(key, Collections.<String> emptyList());
    }

    public static KeyValuesContext build(String key, String value) {
        return build(key, singletonList(value));
    }

    public static KeyValuesContext build(String key, List<String> values) {
        return builder().withKey(key).withValues(values).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<KeyValuesContext> {

        private String key;
        private List<String> values;

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withValues(List<String> values) {
            this.values = values;
            return this;
        }

        @Override
        public KeyValuesContext build() {
            return checkNoNulls(new KeyValuesContext(this));
        }
    }

    public String getKey() {
        return key;
    }

    public ImmutableList<String> getValues() {
        return values;
    }

}
