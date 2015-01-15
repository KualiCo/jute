package org.kuali.common.jute.enc.openssl;

import static com.google.common.io.ByteSource.concat;
import static com.google.common.io.ByteSource.wrap;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.enc.cipher.CipherMode.DECRYPT;
import static org.kuali.common.jute.enc.cipher.CipherMode.ENCRYPT;
import static org.kuali.common.jute.enc.cipher.Ciphers.cipheredCopy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import org.kuali.common.jute.enc.Encryptor;
import org.kuali.common.jute.enc.cipher.CipherMode;

import com.google.common.io.ByteSource;

/**
 *
 * Encrypt/decrypt using the same techniques as OpenSSL. This enables java code to work with data encrypted by OpenSSL (and vice versa)
 *
 * For example the following commands encrypt/decrypt the text "foo" using the password "bar" via OpenSSL:
 *
 * <pre>
 * echo -n "foo" | openssl enc -aes128 -e -base64 -A -k "bar"
 * echo -n "U2FsdGVkX1+VsRny+UbwuLllbAQ5yK/3MenTFJEKRVE=" | openssl enc -aes128 -d -base64 -A -k "bar"
 * </pre>
 */
public final class OpenSSLEncryptor implements Encryptor {

    @Inject
    public OpenSSLEncryptor(OpenSSLContext context) {
        this.context = checkNotNull(context, "context");
    }

    private final OpenSSLContext context;

    @Override
    public String encrypt(String plaintext) {
        ByteSource prefix = wrap(context.getSalt().getPrefix().getBytes(context.getCharset()));
        ByteSource salt = buildSalt(context.getSalt());
        ByteSource secret = wrap(context.getPassword().getBytes(context.getCharset()));
        Cipher cipher = buildCipher(context, ENCRYPT, secret, salt);
        try {
            ByteSource source = wrap(plaintext.getBytes(context.getCharset()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            cipheredCopy(source, out, cipher);
            ByteSource encrypted = wrap(out.toByteArray());
            ByteSource combined = concat(prefix, salt, encrypted);
            return context.getEncoder().encode(combined.read());
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    @Override
    public String decrypt(String encrypted) {
        // decode the string into bytes
        byte[] bytes = context.getEncoder().decode(encrypted);

        // calculate some offsets
        int prefixLength = context.getSalt().getPrefix().length();
        int saltBytes = context.getSalt().getBytes();
        int encryptedOffset = prefixLength + saltBytes;
        int encryptedLength = bytes.length - encryptedOffset;

        // slice up the bytes
        ByteSource all = wrap(bytes);
        ByteSource salt = all.slice(prefixLength, saltBytes);
        ByteSource source = all.slice(encryptedLength, encryptedOffset);

        // setup the cipher
        ByteSource secret = wrap(context.getPassword().getBytes(context.getCharset()));
        Cipher cipher = buildCipher(context, DECRYPT, secret, salt);
        try {
            // decrypt the encrypted bytes
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            cipheredCopy(source, out, cipher);
            byte[] decrypted = out.toByteArray();
            return new String(decrypted, context.getCharset());
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    public OpenSSLContext getContext() {
        return context;
    }

    private static ByteSource buildSalt(OpenSSLSaltContext input) {
        byte[] bytes = new byte[input.getBytes()];
        Random random = (input.isSecure()) ? new SecureRandom() : new Random();
        random.nextBytes(bytes);
        return wrap(bytes);
    }

    private static Cipher buildCipher(OpenSSLContext context, CipherMode mode, ByteSource data, ByteSource salt) {
        try {
            Cipher cipher = Cipher.getInstance(context.getTransformation());
            int initVectorLength = cipher.getBlockSize();
            MessageDigest md = MessageDigest.getInstance(context.getDigest());
            int keyLength = context.getKeyBits() / Byte.SIZE;
            int keyIndex = 0;
            int initVectorIndex = 0;
            byte[] key = new byte[keyLength];
            byte[] initVector = new byte[initVectorLength];
            byte[] digestBuffer = null;
            int i = 0;
            int addDigest = 0;
            for (;;) {
                md.reset();
                if (addDigest++ > 0) {
                    md.update(digestBuffer);
                }
                md.update(data.read());
                if (salt != null) {
                    md.update(salt.read());
                }
                digestBuffer = md.digest();
                for (i = 1; i < context.getIterations(); i++) {
                    md.reset();
                    md.update(digestBuffer);
                    digestBuffer = md.digest();
                }
                i = 0;
                if (keyLength > 0) {
                    for (;;) {
                        if (keyLength == 0) {
                            break;
                        }
                        if (i == digestBuffer.length) {
                            break;
                        }
                        key[keyIndex++] = digestBuffer[i];
                        keyLength--;
                        i++;
                    }
                }
                if (initVectorLength > 0 && i != digestBuffer.length) {
                    for (;;) {
                        if (initVectorLength == 0) {
                            break;
                        }
                        if (i == digestBuffer.length) {
                            break;
                        }
                        initVector[initVectorIndex++] = digestBuffer[i];
                        initVectorLength--;
                        i++;
                    }
                }
                if (keyLength == 0 && initVectorLength == 0) {
                    break;
                }
            }
            for (i = 0; i < digestBuffer.length; i++) {
                digestBuffer[i] = 0;
            }

            Key initKey = new SecretKeySpec(key, context.getEncryption());
            AlgorithmParameterSpec initParams = new IvParameterSpec(initVector);
            cipher.init(mode.getValue(), initKey, initParams);
            return cipher;
        } catch (Exception e) {
            throw illegalState(e);
        }
    }

}
