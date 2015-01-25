package org.kuali.common.jute.env.filter;

import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.base.Predicates.includesExcludes;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;

import javax.inject.Provider;

import org.kuali.common.jute.env.Environment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

@JsonDeserialize(builder = PredicateProvider.Builder.class)
public final class PredicateProvider implements Provider<Predicate<CharSequence>> {

    private final Environment env;
    private final KeyValues includes;
    private final KeyValues excludes;
    private final Predicate<CharSequence> absentDetector;
    private final Splitter splitter;

    @Override
    public Predicate<CharSequence> get() {
        List<String> includes = buildList(env, this.includes);
        List<String> excludes = buildList(env, this.excludes);
        return includesExcludes(includes, excludes);
    }

    private List<String> buildList(Environment env, KeyValues context) {
        String csv = trimToNull(env.getProperty(context.getKey(), null));
        if (absentDetector.apply(csv)) {
            return context.getValues();
        } else {
            return splitter.splitToList(csv);
        }
    }

    private PredicateProvider(Builder builder) {
        this.includes = builder.includes;
        this.excludes = builder.excludes;
        this.absentDetector = builder.absentDetector;
        this.splitter = builder.splitter;
        this.env = builder.env;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<PredicateProvider> {

        private Environment env;
        private KeyValues includes;
        private KeyValues excludes;
        private Predicate<CharSequence> absentDetector;
        private Splitter splitter;

        public Builder withEnv(Environment env) {
            this.env = env;
            return this;
        }

        public Builder withIncludes(KeyValues includes) {
            this.includes = includes;
            return this;
        }

        public Builder withExcludes(KeyValues excludes) {
            this.excludes = excludes;
            return this;
        }

        public Builder withAbsentDetector(Predicate<CharSequence> absentDetector) {
            this.absentDetector = absentDetector;
            return this;
        }

        public Builder withSplitter(Splitter splitter) {
            this.splitter = splitter;
            return this;
        }

        @Override
        public PredicateProvider build() {
            return checkNoNulls(new PredicateProvider(this));
        }
    }

    public Environment getEnv() {
        return env;
    }

    public KeyValues getIncludes() {
        return includes;
    }

    public KeyValues getExcludes() {
        return excludes;
    }

    public Predicate<CharSequence> getAbsentDetector() {
        return absentDetector;
    }

    public Splitter getSplitter() {
        return splitter;
    }

}
