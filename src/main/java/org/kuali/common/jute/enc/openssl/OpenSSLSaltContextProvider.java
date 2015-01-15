package org.kuali.common.jute.enc.openssl;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.env.Environment;

public final class OpenSSLSaltContextProvider implements Provider<OpenSSLSaltContext> {

    @Inject
    public OpenSSLSaltContextProvider(Environment env) {
        this.env = checkNotNull(env, "env");
    }

    private final Environment env;

    @Override
    public OpenSSLSaltContext get() {
        OpenSSLSaltContext.Builder builder = OpenSSLSaltContext.builder();
        builder.withBytes(parseInt(env.getProperty("openssl.salt.bytes", builder.getBytes() + "")));
        builder.withPrefix(env.getProperty("openssl.salt.prefix", builder.getPrefix()));
        builder.withSecure(parseBoolean(env.getProperty("openssl.salt.secure", builder.isSecure() + "")));
        return builder.build();
    }

}
