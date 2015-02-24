package org.kuali.common.jute.base;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Strings.emptyToNull;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.base.Precondition.checkMax;

import java.util.Date;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public final class Optionals {

    private Optionals() {}

    private static final long NEGATIVE_ONE = -1;

    public static <T> Function<Optional<T>, T> to() {
        return new ToFunction<T>();
    }

    private static class ToFunction<T> implements Function<Optional<T>, T> {

        @Override
        public T apply(Optional<T> input) {
            return input.get();
        }

    }

    public static <T> Predicate<Optional<T>> isPresent() {
        return new PresentPredicate<T>();
    }

    private static class PresentPredicate<T> implements Predicate<Optional<T>> {

        @Override
        public boolean apply(Optional<T> input) {
            return input.isPresent();
        }

    }

    public static Optional<Integer> optionalInteger(String text) {
        Optional<String> optional = fromTrimToNull(text);
        if (optional.isPresent()) {
            int value = parseInt(optional.get());
            return Optional.of(value);
        } else {
            return absent();
        }
    }

    public static Optional<Integer> fromMissingOrZeroToAbsent(String text) {
        Optional<Integer> optional = optionalInteger(text);
        if (optional.isPresent() && optional.get() == 0) {
            return absent();
        } else {
            return optional;
        }
    }

    public static int countPresent(Optional<?>... optionals) {
        int present = 0;
        for (Optional<?> optional : optionals) {
            present = optional.isPresent() ? (present + 1) : present;
        }
        return present;
    }

    /**
     * Returns {@code -1} if {@code optional.isPresent() == false}, otherwise returns {@code optional.get()}
     */
    public static long fromAbsentToNegative(Optional<Long> optional) {
        return fromAbsentToNegative(optional, NEGATIVE_ONE);
    }

    /**
     * Returns {@code negativeValue} if {@code optional.isPresent() == false}, otherwise returns {@code optional.get()}
     */
    public static long fromAbsentToNegative(Optional<Long> optional, long negativeValue) {
        checkMax(negativeValue, NEGATIVE_ONE, "negativeValue");
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return negativeValue;
        }
    }

    /**
     * Returns {@code Optional.absent()} if {@code value < 0}, otherwise {@code Optional.of(value)}
     */
    public static Optional<Double> fromNegativeToAbsent(double value) {
        if (value < 0) {
            return absent();
        } else {
            return Optional.of(value);
        }
    }

    /**
     * Returns {@code Optional.absent()} if {@code value < 0}, otherwise {@code Optional.of(value)}
     */
    public static Optional<Long> fromNegativeToAbsent(long value) {
        if (value < 0) {
            return absent();
        } else {
            return Optional.of(value);
        }
    }

    /**
     * Returns {@code Optional.of(value)} if {@code value > 0}, otherwise {@code Optional.absent()}
     */
    public static Optional<Integer> fromPositiveToPresent(int value) {
        if (value > 0) {
            return Optional.of(value);
        } else {
            return absent();
        }
    }

    /**
     * Returns {@code Optional.absent()} if {@code value == 0}, otherwise {@code Optional.of(value)}
     */
    public static Optional<Integer> fromZeroToAbsent(int value) {
        if (value == 0) {
            return absent();
        } else {
            return Optional.of(value);
        }
    }

    /**
     * Returns {@code Optional.absent()} if {@code value < 0}, otherwise {@code Optional.of(value)}
     */
    public static Optional<Integer> fromNegativeToAbsent(int value) {
        if (value < 0) {
            return absent();
        } else {
            return Optional.of(value);
        }
    }

    /**
     * Returns {@code Optional.absent()} if {@code date == null}, otherwise {@code Optional.of(date.getTime())}
     */
    public static Optional<Long> fromNullableDate(Date date) {
        if (date == null) {
            return absent();
        } else {
            return Optional.of(date.getTime());
        }
    }

    /**
     * Returns {@code Optional.absent()} if {@code StringUtils.trimToNull(s)} returns {@code null}, otherwise {@code Optional.of(s)}
     */
    public static Optional<String> fromTrimToNull(String s) {
        return fromNullable(trimToNull(s));
    }

    /**
     * Returns {@code Optional.absent()} if {@code Strings.emptyToNull(s)} returns {@code null}, otherwise {@code Optional.of(s)}
     */
    public static Optional<String> fromEmptyToNull(String s) {
        return fromNullable(emptyToNull(s));
    }

}
