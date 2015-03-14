package org.kuali.common.jute.base;

import com.google.common.base.Function;

public final class Functions {

    private Functions() {}

    public static Function<String, String> lowerCaseFunction() {
        return LowerCaseFunction.INSTANCE;
    }

    private enum LowerCaseFunction implements Function<String, String> {
        INSTANCE;

        @Override
        public String apply(String input) {
            return input.toLowerCase();
        }
    }

}
