package org.kuali.common.jute.base;

import static com.google.common.collect.Ordering.natural;

import com.google.common.collect.Ordering;

public final class Orderings {

    public static final Ordering<String> NULLS_FIRST = nullsFirst();
    public static final Ordering<Integer> NULL_INTEGERS_FIRST = nullsFirst();

    public static <T extends Comparable<T>> Ordering<T> nullsFirst() {
        return natural().nullsFirst();
    }

}
