package org.kuali.common.jute.guice;

import com.google.common.base.Optional;
import com.google.inject.TypeLiteral;

public final class TypeLiterals {

    private TypeLiterals() {}

    public static TypeLiteral<Optional<String>> optionalString() {
        return new TypeLiteral<Optional<String>>() {};
    }

}
