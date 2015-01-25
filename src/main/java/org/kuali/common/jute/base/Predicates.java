package org.kuali.common.jute.base;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.isEmpty;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import com.google.common.base.Predicate;

public final class Predicates {

    private Predicates() {};

    private static final Predicate<String> ALWAYS_TRUE = alwaysTrue();
    private static final Predicate<String> ALWAYS_FALSE = alwaysFalse();

    public static Predicate<String> includesExcludes(Iterable<String> includes, Iterable<String> excludes) {
        checkNotNull(includes, "includes");
        checkNotNull(excludes, "excludes");
        Predicate<String> incl = isEmpty(includes) ? ALWAYS_TRUE : containsAny(includes);
        Predicate<String> excl = isEmpty(excludes) ? ALWAYS_FALSE : containsAny(excludes);
        return and(incl, not(excl));
    }

    public static Predicate<String> containsAny(Iterable<String> patterns) {
        checkNotNull(patterns, "patterns");
        Predicate<String> predicate = alwaysFalse();
        for (String pattern : patterns) {
            predicate = or(containsPattern(pattern), predicate);
        }
        return predicate;
    }
}
