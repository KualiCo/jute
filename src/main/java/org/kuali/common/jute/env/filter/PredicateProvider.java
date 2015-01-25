package org.kuali.common.jute.env.filter;

import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.base.Predicates.includesExcludes;
import static org.kuali.common.jute.env.Environments.absentDetector;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.env.filter.annotation.AbsentDetector;
import org.kuali.common.jute.env.filter.annotation.CsvSplitter;
import org.kuali.common.maven.project.annotation.PropertyFilterContext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

@JsonDeserialize(builder = PredicateProvider.Builder.class)
public final class PredicateProvider implements Provider<Predicate<CharSequence>> {

    private final Environment env;
    private final FilterContext context;
    private final Predicate<CharSequence> absentDetector;
    private final Splitter splitter;

    @Override
    public Predicate<CharSequence> get() {
        List<String> includes = buildList(env, context.getIncludes());
        List<String> excludes = buildList(env, context.getExcludes());
        return includesExcludes(includes, excludes);
    }

    private List<String> buildList(Environment env, KeyValuesContext context) {
        String csv = trimToNull(env.getProperty(context.getKey(), null));
        if (absentDetector.apply(csv)) {
            return context.getValues();
        } else {
            return splitter.splitToList(csv);
        }
    }

    private PredicateProvider(Builder builder) {
        this.env = builder.env;
        this.context = builder.context;
        this.absentDetector = builder.absentDetector;
        this.splitter = builder.splitter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<PredicateProvider>, Provider<PredicateProvider> {

        private Environment env;
        private Splitter splitter;
        private FilterContext context;
        private Predicate<CharSequence> absentDetector = absentDetector();

        @Inject
        public Builder withSplitter(@CsvSplitter Splitter splitter) {
            this.splitter = splitter;
            return this;
        }

        @Inject
        public Builder withEnv(Environment env) {
            this.env = env;
            return this;
        }

        @Inject
        public Builder withContext(@PropertyFilterContext FilterContext context) {
            this.context = context;
            return this;
        }

        @Inject
        public Builder withAbsentDetector(@AbsentDetector Predicate<CharSequence> absentDetector) {
            this.absentDetector = absentDetector;
            return this;
        }

        @Override
        public PredicateProvider get() {
            return build();
        }

        @Override
        public PredicateProvider build() {
            return checkNoNulls(new PredicateProvider(this));
        }
    }

    public Environment getEnv() {
        return env;
    }

    public FilterContext getContext() {
        return context;
    }

    public Splitter getSplitter() {
        return splitter;
    }

    public Predicate<CharSequence> getAbsentDetector() {
        return absentDetector;
    }

}
