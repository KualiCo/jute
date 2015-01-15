package org.kuali.common.jute.enc.openssl;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.BaseEncoding.base64;
import static org.kuali.common.jute.enc.EncryptionPasswordProvider.DEFAULT_ENCRYPTION_PASSWORD;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.enc.EncryptionPassword;
import org.kuali.common.jute.enc.openssl.annotation.OpenSSLCharset;
import org.kuali.common.jute.enc.openssl.annotation.OpenSSLEncoder;

import com.google.common.io.BaseEncoding;

public final class OpenSSLContext {

    private final String digest;
    private final String encryption;
    private final String transformation;
    private final int keyBits;
    private final int iterations;
    private final String password;
    private final OpenSSLSaltContext salt;
    private final BaseEncoding encoder;
    private final Charset charset;

    private OpenSSLContext(Builder builder) {
        this.digest = builder.digest;
        this.encryption = builder.encryption;
        this.transformation = builder.transformation;
        this.keyBits = builder.keyBits;
        this.iterations = builder.iterations;
        this.password = builder.password;
        this.salt = builder.salt;
        this.encoder = builder.encoder;
        this.charset = builder.charset;
    }

    public static OpenSSLContext build() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<OpenSSLContext>, Provider<OpenSSLContext> {

        private String digest = "MD5";
        private String encryption = "AES";
        private String transformation = "AES/CBC/PKCS5Padding";
        private int keyBits = 128;
        private int iterations = 1;
        private String password = DEFAULT_ENCRYPTION_PASSWORD;
        private OpenSSLSaltContext salt = OpenSSLSaltContext.build();
        private BaseEncoding encoder = base64();
        private Charset charset = UTF_8;

        public Builder withDigest(String digest) {
            this.digest = digest;
            return this;
        }

        public Builder withEncryption(String encryption) {
            this.encryption = encryption;
            return this;
        }

        public Builder withTransformation(String transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder withKeyBits(int keyBits) {
            this.keyBits = keyBits;
            return this;
        }

        public Builder withIterations(int iterations) {
            this.iterations = iterations;
            return this;
        }

        @Inject
        public Builder withCharset(@OpenSSLCharset Charset charset) {
            this.charset = charset;
            return this;
        }

        @Inject
        public Builder withPassword(@EncryptionPassword String password) {
            this.password = password;
            return this;
        }

        @Inject
        public Builder withSalt(OpenSSLSaltContext salt) {
            this.salt = salt;
            return this;
        }

        @Inject
        public Builder withEncoder(@OpenSSLEncoder BaseEncoding encoder) {
            this.encoder = encoder;
            return this;
        }

        @Override
        public OpenSSLContext get() {
            return build();
        }

        @Override
        public OpenSSLContext build() {
            return checkNoNulls(new OpenSSLContext(this));
        }
    }

    public String getDigest() {
        return digest;
    }

    public String getEncryption() {
        return encryption;
    }

    public String getTransformation() {
        return transformation;
    }

    public int getKeyBits() {
        return keyBits;
    }

    public int getIterations() {
        return iterations;
    }

    public String getPassword() {
        return password;
    }

    public OpenSSLSaltContext getSalt() {
        return salt;
    }

    public BaseEncoding getEncoder() {
        return encoder;
    }

    public Charset getCharset() {
        return charset;
    }

}
