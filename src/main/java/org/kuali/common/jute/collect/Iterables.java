package org.kuali.common.jute.collect;

import static com.google.common.collect.Iterables.transform;
import static org.kuali.common.jute.base.Precondition.checkSize;

import org.kuali.common.jute.base.Reducer;

import com.google.common.base.Function;

public class Iterables {

    /**
     * Check that the iterable contains exactly one element and return it
     *
     * @throws IllegalArgumentException
     *             if the iterable is empty or contains more than one element
     */
    public static <T> T getSingleElement(Iterable<T> iterable) {
        return checkSize(iterable, 1).iterator().next();
    }

    public static <A, B, C> C mapreduce(Iterable<A> iterable, Function<A, B> mapper, C initial, Reducer<B, C> reducer) {
        Iterable<B> transformed = transform(iterable, mapper);
        return reduce(transformed, initial, reducer);
    }

    public static <A, B> B reduce(Iterable<A> iterable, B initial, Reducer<A, B> reducer) {
        B value = initial;
        for (A element : iterable) {
            value = reducer.apply(value, element);
        }
        return value;
    }

}
