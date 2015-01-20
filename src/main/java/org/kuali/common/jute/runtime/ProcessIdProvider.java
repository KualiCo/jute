package org.kuali.common.jute.runtime;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Iterables.getFirst;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.lang.management.ManagementFactory;

import javax.inject.Provider;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;

public enum ProcessIdProvider implements Provider<Optional<Integer>> {
    INSTANCE;

    @Override
    public Optional<Integer> get() {
        try {
            // From the Javadoc -> "The returned name string can be any arbitrary string"
            // In practice, the name is *usually* the pid followed by the @ character, but there are no guarantees
            String name = ManagementFactory.getRuntimeMXBean().getName();
            Iterable<String> tokens = Splitter.on('@').split(name);
            String first = getFirst(tokens, null);
            String trimmed = trimToNull(first);
            if (trimmed == null) {
                return absent();
            } else {
                int pid = parseInt(trimmed);
                return Optional.of(pid);
            }
        } catch (Throwable e) {
            // If anything goes wrong at any point, just return absent()
            return absent();
        }
    }

}
