package org.kuali.common.jute.enc.openssl;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

public final class OpenSSLSaltContext {

    private final String prefix;
    private final int bytes;
    private final boolean secure;

    private OpenSSLSaltContext(Builder builder) {
        this.prefix = builder.prefix;
        this.bytes = builder.bytes;
        this.secure = builder.secure;
    }

    public static OpenSSLSaltContext build() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<OpenSSLSaltContext> {

        private String prefix = "Salted__";
        private int bytes = 8;
        private boolean secure = true;

        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder withBytes(int bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder withSecure(boolean secure) {
            this.secure = secure;
            return this;
        }

        @Override
        public OpenSSLSaltContext build() {
            return checkNoNulls(new OpenSSLSaltContext(this));
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public int getBytes() {
            return bytes;
        }

        public void setBytes(int bytes) {
            this.bytes = bytes;
        }

        public boolean isSecure() {
            return secure;
        }

        public void setSecure(boolean secure) {
            this.secure = secure;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public int getBytes() {
        return bytes;
    }

    public boolean isSecure() {
        return secure;
    }

}
