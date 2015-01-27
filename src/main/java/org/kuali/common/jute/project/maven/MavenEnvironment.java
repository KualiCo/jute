package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkState;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.collect.ImmutableProperties;
import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.project.maven.annotation.EnvPrefix;
import org.kuali.common.jute.project.maven.annotation.ProjectProperties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

/**
 * Maven resolves property values using system properties, environment variables, and project properties.
 *
 * It looks up environment variables using the prefix 'env'.
 */
@JsonDeserialize(builder = MavenEnvironment.Builder.class)
public final class MavenEnvironment implements Environment {

    private final Environment env;
    private final ImmutableProperties properties;
    private final String prefix;

    @Override
    public Optional<String> getProperty(String key) {
        Optional<String> standard = env.getProperty(key);
        if (standard.isPresent()) {
            return standard;
        }
        Optional<String> prefixed = env.getProperty(prefix + "." + key);
        if (prefixed.isPresent()) {
            return prefixed;
        }
        return fromNullable(properties.getProperty(key));
    }

    @Override
    public boolean containsProperty(String key) {
        return getProperty(key).isPresent();
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        Optional<String> value = getProperty(key);
        if (value.isPresent()) {
            return value.get();
        } else {
            return defaultValue;
        }
    }

    @Override
    public String getRequiredProperty(String key) {
        Optional<String> value = getProperty(key);
        checkState(value.isPresent(), "'%s' is required", key);
        return value.get();
    }

    public Environment getEnv() {
        return env;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getPrefix() {
        return prefix;
    }

    private MavenEnvironment(Builder builder) {
        this.env = builder.env;
        this.properties = ImmutableProperties.copyOf(builder.properties);
        this.prefix = builder.prefix;
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<MavenEnvironment>, Provider<MavenEnvironment> {

        private Environment env;
        private Properties properties;
        private String prefix;

        @Inject
        public Builder withEnv(Environment env) {
            this.env = env;
            return this;
        }

        @Inject
        public Builder withProperties(@ProjectProperties Properties properties) {
            this.properties = properties;
            return this;
        }

        @Inject
        public Builder withPrefix(@EnvPrefix String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Override
        public MavenEnvironment get() {
            return build();
        }

        @Override
        public MavenEnvironment build() {
            return checkNoNulls(new MavenEnvironment(this));
        }
    }

}
