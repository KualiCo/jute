package org.kuali.common.jute.inject;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.inject.TypeLiteral;

public final class TypeLiterals {

    private TypeLiterals() {}

    public static TypeLiteral<List<Function<String, String>>> stringToStringFunctionList() {
        return new TypeLiteral<List<Function<String, String>>>() {};
    }

    public static TypeLiteral<Optional<Integer>> optionalInteger() {
        return new TypeLiteral<Optional<Integer>>() {};
    }

    public static TypeLiteral<Optional<Double>> optionalDouble() {
        return new TypeLiteral<Optional<Double>>() {};
    }

    public static TypeLiteral<Optional<String>> optionalString() {
        return new TypeLiteral<Optional<String>>() {};
    }

}
