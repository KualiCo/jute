package org.kuali.common.jute.env.filter;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.env.Environments.ABSENT;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import javax.inject.Provider;

import org.kuali.common.jute.env.Environment;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

public final class PredicateProvider implements Provider<Predicate<String>> {

    private final Environment env;
    private final FilterContext context;
    private final Predicate<String> absenceDetector;

    @Override
    public Predicate<String> get() {
        return buildPredicate();
    }

    private Predicate<String> buildPredicate() {
        String iKey = context.getIncludes().getKey();
        String iVal = trimToNull(Joiner.on(',').join(context.getIncludes().getValues()));
        String eKey = context.getExcludes().getKey();
        String eVal = trimToNull(Joiner.on(',').join(context.getExcludes().getValues()));
        Predicate<String> includes = buildPredicate(iKey, iVal, true);
        Predicate<String> excludes = buildPredicate(eKey, eVal, false);
        return and(includes, not(excludes));
    }

    private Predicate<String> buildPredicate(String key, String provided, boolean matchIfAbsent) {
        Predicate<String> alwaysTrue = alwaysTrue();
        Predicate<String> alwaysFalse = alwaysFalse();
        String csv = trimToNull(env.getProperty(key, provided));
        if (absenceDetector.apply(csv)) {
            return (matchIfAbsent) ? alwaysTrue : alwaysFalse;
        }
        Iterable<String> tokens = Splitter.on(',').omitEmptyStrings().trimResults().split(csv);
        Predicate<String> predicate = alwaysFalse;
        for (String token : tokens) {
            predicate = or(containsPattern(token), predicate);
        }
        return predicate;
    }

    private PredicateProvider(Builder builder) {
        this.env = builder.env;
        this.context = builder.context;
        this.absenceDetector = builder.absenceDetector;
    }

    private enum AbsentPredicate implements Predicate<String> {
        INSTANCE;

        @Override
        public boolean apply(String input) {
            return (input == null) || input.equals(ABSENT);
        }
    }

    public static PredicateProvider build(Environment env, FilterContext context) {
        return builder().withEnv(env).withContext(context).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<PredicateProvider> {

        private Environment env;
        private FilterContext context;
        private Predicate<String> absenceDetector = AbsentPredicate.INSTANCE;

        public Builder withEnv(Environment env) {
            this.env = env;
            return this;
        }

        public Builder withContext(FilterContext context) {
            this.context = context;
            return this;
        }

        public Builder withAbsenceDetector(Predicate<String> absenceDetector) {
            this.absenceDetector = absenceDetector;
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

    public FilterContext getContext() {
        return context;
    }

    public Predicate<String> getAbsenceDetector() {
        return absenceDetector;
    }

}
