package org.kuali.common.jute.kfs;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Ordering.natural;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.base.Optionals;
import org.kuali.common.jute.kfs.annotation.DepsContainer;

import com.google.common.base.Function;
import com.google.common.base.Optional;

public final class DepFragmentsProvider implements Provider<List<String>> {

    @Inject
    public DepFragmentsProvider(@DepsContainer List<List<String>> container) {
        this.container = container;
    }

    private final List<List<String>> container;

    @Override
    public List<String> get() {
        List<Optional<String>> list = transform(container, VersionPropertyFunction.INSTANCE);
        List<Optional<String>> present = copyOf(filter(list, Optionals.<String> isPresent()));
        return natural().immutableSortedCopy(transform(present, Optionals.<String> to()));
    }

    private enum VersionPropertyFunction implements Function<List<String>, Optional<String>> {
        INSTANCE;

        @Override
        public Optional<String> apply(List<String> input) {
            String artifactId = null;
            String version = null;
            for (String line : input) {
                if (line.contains("<artifactId>")) {
                    artifactId = substringBetween(line, "<artifactId>", "</artifactId>");
                }
                if (line.contains("<version>")) {
                    version = substringBetween(line, "<version>", "</version>");
                    if (version.contains("${")) {
                        return absent();
                    } else {
                        String property = "<" + artifactId + ".version>" + version + "</" + artifactId + ".version>";
                        return Optional.of(property);
                    }
                }
            }
            throw illegalState("could not parse version");
        }
    }

}
