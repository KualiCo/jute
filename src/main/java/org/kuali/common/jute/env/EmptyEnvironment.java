package org.kuali.common.jute.env;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import com.google.common.base.Optional;

public enum EmptyEnvironment implements Environment {
    INSTANCE;

    @Override
    public Optional<String> getProperty(String key) {
        return absent();
    }

    @Override
    public boolean containsProperty(String key) {
        return false;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return defaultValue;
    }

    @Override
    public String getRequiredProperty(String key) {
        throw illegalState("'%s' is required", key);
    }

}
