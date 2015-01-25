package org.kuali.common.jute.env.filter;

import static java.util.Collections.singletonList;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = KeyValues.Builder.class)
public final class KeyValues {

    private final String key;
    private final ImmutableList<String> values;

    private KeyValues(Builder builder) {
        this.key = builder.key;
        this.values = ImmutableList.copyOf(builder.values);
    }

    public static KeyValues build(String key) {
        return build(key, Collections.<String> emptyList());
    }

    public static KeyValues build(String key, String value) {
        return build(key, singletonList(value));
    }

    public static KeyValues build(String key, List<String> values) {
        return builder().withKey(key).withValues(values).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<KeyValues> {

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
        public KeyValues build() {
            return checkNoNulls(new KeyValues(this));
        }
    }

    public String getKey() {
        return key;
    }

    public List<String> getValues() {
        return values;
    }

}
