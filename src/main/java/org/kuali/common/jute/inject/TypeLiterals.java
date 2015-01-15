package org.kuali.common.jute.inject;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.inject.TypeLiteral;

public final class TypeLiterals {

    private TypeLiterals() {
    }

    public static StringToStringFunctionList stringToStringFunctionList() {
        return new StringToStringFunctionList();
    }

    public static OptionalInteger optionalInteger() {
        return new OptionalInteger();
    }

    public static OptionalDouble optionalDouble() {
        return new OptionalDouble();
    }

    public static OptionalString optionalString() {
        return new OptionalString();
    }

    private static final class StringToStringFunctionList extends TypeLiteral<List<Function<String, String>>> {
    }

    private static final class OptionalInteger extends TypeLiteral<Optional<Integer>> {
    }

    private static final class OptionalString extends TypeLiteral<Optional<String>> {
    }

    private static final class OptionalDouble extends TypeLiteral<Optional<Double>> {
    }

}
