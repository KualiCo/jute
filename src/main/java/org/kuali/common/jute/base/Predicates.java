package org.kuali.common.jute.base;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.List;

import com.google.common.base.Predicate;

public final class Predicates {

    private Predicates() {};

    private static final Predicate<CharSequence> ALWAYS_TRUE = alwaysTrue();
    private static final Predicate<CharSequence> ALWAYS_FALSE = alwaysFalse();

    public static Predicate<CharSequence> includesExcludes(Iterable<String> includes, Iterable<String> excludes) {
        checkNotNull(includes, "includes");
        checkNotNull(excludes, "excludes");
        Predicate<CharSequence> incl = isEmpty(includes) ? ALWAYS_TRUE : containsAnyPattern(includes);
        Predicate<CharSequence> excl = isEmpty(excludes) ? ALWAYS_FALSE : containsAnyPattern(excludes);
        return and(incl, not(excl));
    }

    public static Predicate<CharSequence> containsAnyPattern(Iterable<String> patterns) {
        checkNotNull(patterns, "patterns");
        List<Predicate<CharSequence>> predicates = newArrayList();
        for (String pattern : patterns) {
            Predicate<CharSequence> predicate = containsPattern(pattern);
            predicates.add(predicate);
        }
        return or(predicates);
    }
}
