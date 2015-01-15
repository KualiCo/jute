package org.kuali.common.jute.enc.openssl;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.BaseEncoding.base64;

import java.nio.charset.Charset;

import org.kuali.common.jute.enc.EncryptionPassword;
import org.kuali.common.jute.enc.EncryptionPasswordProvider;
import org.kuali.common.jute.enc.Encryptor;
import org.kuali.common.jute.enc.openssl.annotation.OpenSSLCharset;
import org.kuali.common.jute.enc.openssl.annotation.OpenSSLEncoder;

import com.google.common.io.BaseEncoding;
import com.google.inject.AbstractModule;

public class OpenSSLModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(EncryptionPassword.class).toProvider(EncryptionPasswordProvider.class).asEagerSingleton();
        bind(OpenSSLSaltContext.class).toProvider(OpenSSLSaltContextProvider.class).asEagerSingleton();
        bind(BaseEncoding.class).annotatedWith(OpenSSLEncoder.class).toInstance(base64());
        bind(Charset.class).annotatedWith(OpenSSLCharset.class).toInstance(UTF_8);
        bind(OpenSSLContext.class).toProvider(OpenSSLContext.Builder.class).asEagerSingleton();
        bind(Encryptor.class).to(OpenSSLEncryptor.class).asEagerSingleton();
    }
}
