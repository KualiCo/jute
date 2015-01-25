package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkState;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.Properties;

import javax.inject.Inject;

import org.kuali.common.jute.collect.ImmutableProperties;
import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.project.maven.annotation.EnvPrefix;
import org.kuali.common.jute.project.maven.annotation.ProjectProperties;

import com.google.common.base.Optional;

public final class MavenEnvironment implements Environment {

    @Inject
    public MavenEnvironment(Environment env, @ProjectProperties Properties properties, @EnvPrefix String prefix) {
        this.env = checkNotNull(env, "env");
        this.properties = ImmutableProperties.copyOf(properties);
        this.prefix = checkNotBlank(prefix, "prefix");
    }

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

}
