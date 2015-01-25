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

    /**
     * 1 - Return false if there is a match on any of the exclude patterns.<br>
     * 2 - Return true if no include patterns were provided.<br>
     * 3 - Return true if there is a match on one of the include patterns.<br>
     */
    public static Predicate<CharSequence> includesExcludes(Iterable<String> includes, Iterable<String> excludes) {
        checkNotNull(includes, "includes");
        checkNotNull(excludes, "excludes");
        Predicate<CharSequence> incl = isEmpty(includes) ? ALWAYS_TRUE : containsAny(includes);
        Predicate<CharSequence> excl = isEmpty(excludes) ? ALWAYS_FALSE : containsAny(excludes);
        return and(incl, not(excl));
    }

    /**
     * Return true if there is a match on one (or more) of the patterns, false otherwise.
     */
    public static Predicate<CharSequence> containsAny(Iterable<String> patterns) {
        checkNotNull(patterns, "patterns");
        List<Predicate<CharSequence>> predicates = newArrayList();
        for (String pattern : patterns) {
            Predicate<CharSequence> predicate = containsPattern(pattern);
            predicates.add(predicate);
        }
        return or(predicates);
    }
}
