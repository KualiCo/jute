package org.kuali.common.jute.enc.cipher;

import javax.crypto.Cipher;

public enum CipherMode {

    ENCRYPT(Cipher.ENCRYPT_MODE), //
    DECRYPT(Cipher.DECRYPT_MODE), //
    WRAP(Cipher.WRAP_MODE), //
    UNWRAP(Cipher.UNWRAP_MODE); //

    private final int value;

    private CipherMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
