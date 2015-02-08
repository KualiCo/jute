package org.kuali.common.jute.base;

import static com.google.common.base.Ascii.isLowerCase;
import static com.google.common.base.Ascii.isUpperCase;

public final class Ascii {

    private Ascii() {}

    public static boolean isLetter(char c) {
        return isLowerCase(c) || isUpperCase(c);
    }

    public static boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }

}
