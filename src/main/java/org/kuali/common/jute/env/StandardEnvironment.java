package org.kuali.common.jute.env;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkState;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.List;

import javax.inject.Inject;

import org.kuali.common.jute.system.VirtualSystem;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public final class StandardEnvironment implements Environment {

    @Inject
    public StandardEnvironment(VirtualSystem system, @AlternateKeyFunctions List<Function<String, String>> alternateKeyFunctions) {
        this.system = checkNotNull(system, "system");
        this.alternateKeyFunctions = ImmutableList.copyOf(alternateKeyFunctions);
    }

    private final VirtualSystem system;
    private final ImmutableList<Function<String, String>> alternateKeyFunctions;

    public VirtualSystem getSystem() {
        return system;
    }

    @Override
    public Optional<String> getProperty(String key) {
        checkNotBlank(key, "key");
        Optional<String> sys = fromNullable(system.getProperties().getProperty(key));
        if (sys.isPresent()) {
            return sys;
        }
        Optional<String> env = fromNullable(system.getEnvironment().getProperty(key));
        if (env.isPresent()) {
            return env;
        }
        for (Function<String, String> alternateKeyFunction : alternateKeyFunctions) {
            String alternateKey = alternateKeyFunction.apply(key);
            env = fromNullable(system.getEnvironment().getProperty(alternateKey));
            if (env.isPresent()) {
                return env;
            }
        }
        return absent();
    }

    @Override
    public boolean containsProperty(String key) {
        checkNotBlank(key, "key");
        return getProperty(key).isPresent();
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        checkNotBlank(key, "key");
        Optional<String> value = getProperty(key);
        if (value.isPresent()) {
            return value.get();
        }
        return defaultValue;
    }

    @Override
    public String getRequiredProperty(String key) {
        Optional<String> value = getProperty(key);
        checkState(value.isPresent(), "'%s' is required", key);
        return value.get();
    }

}
