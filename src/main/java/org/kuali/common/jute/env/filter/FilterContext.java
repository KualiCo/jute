package org.kuali.common.jute.env.filter;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

public final class FilterContext {

    private final KeyValuesContext includes;
    private final KeyValuesContext excludes;

    private FilterContext(Builder builder) {
        this.includes = builder.includes;
        this.excludes = builder.excludes;
    }

    public static FilterContext build(String key, String include) {
        String includeKey = key + ".includes";
        String excludeKey = key + ".excludes";
        KeyValuesContext includes = KeyValuesContext.build(includeKey, include);
        KeyValuesContext excludes = KeyValuesContext.build(excludeKey);
        return builder().withIncludes(includes).withExcludes(excludes).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<FilterContext> {

        private KeyValuesContext includes;
        private KeyValuesContext excludes;

        public Builder withIncludes(KeyValuesContext includes) {
            this.includes = includes;
            return this;
        }

        public Builder withExcludes(KeyValuesContext excludes) {
            this.excludes = excludes;
            return this;
        }

        @Override
        public FilterContext build() {
            return checkNoNulls(new FilterContext(this));
        }
    }

    public KeyValuesContext getIncludes() {
        return includes;
    }

    public KeyValuesContext getExcludes() {
        return excludes;
    }

}
