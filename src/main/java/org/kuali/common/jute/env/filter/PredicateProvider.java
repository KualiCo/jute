package org.kuali.common.jute.env.filter;

import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.base.Predicates.includesExcludes;
import static org.kuali.common.jute.env.Environments.absentPredicate;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.env.filter.annotation.AbsenceDetector;
import org.kuali.common.jute.env.filter.annotation.CsvSplitter;
import org.kuali.common.jute.process.ProcessContext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

@JsonDeserialize(builder = ProcessContext.Builder.class)
public final class PredicateProvider implements Provider<Predicate<CharSequence>> {

    private final Environment env;
    private final FilterContext context;
    private final Predicate<CharSequence> absenceDetector;
    private final Splitter splitter;

    @Override
    public Predicate<CharSequence> get() {
        return buildPredicate();
    }

    private Predicate<CharSequence> buildPredicate() {
        List<String> includes = buildList(env, context.getIncludes());
        List<String> excludes = buildList(env, context.getExcludes());
        return includesExcludes(includes, excludes);
    }

    private List<String> buildList(Environment env, KeyValuesContext context) {
        String csv = trimToNull(env.getProperty(context.getKey(), null));
        if (absenceDetector.apply(csv)) {
            return context.getValues();
        } else {
            return splitter.splitToList(csv);
        }
    }

    private PredicateProvider(Builder builder) {
        this.env = builder.env;
        this.context = builder.context;
        this.absenceDetector = builder.absenceDetector;
        this.splitter = builder.splitter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<PredicateProvider>, Provider<PredicateProvider> {

        private Environment env;
        private Splitter splitter;
        private FilterContext context;
        private Predicate<CharSequence> absenceDetector = absentPredicate();

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
        public Builder withContext(FilterContext context) {
            this.context = context;
            return this;
        }

        @Inject
        public Builder withAbsenceDetector(@AbsenceDetector Predicate<CharSequence> absenceDetector) {
            this.absenceDetector = absenceDetector;
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

    public Predicate<CharSequence> getAbsenceDetector() {
        return absenceDetector;
    }

    public Splitter getSplitter() {
        return splitter;
    }

}
