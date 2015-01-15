package org.kuali.common.jute.enc;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Optional.absent;
import static com.google.common.io.Files.asCharSource;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Optionals.fromTrimToNull;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.system.User;

import com.google.common.base.Optional;

public final class EncryptionPasswordProvider implements Provider<String> {

    public static final String DEFAULT_ENCRYPTION_PASSWORD = "password";

    @Inject
    public EncryptionPasswordProvider(Environment env, User user) {
        this.env = checkNotNull(env, "env");
        this.user = checkNotNull(user, "user");
    }

    private final Environment env;
    private final User user;

    @Override
    public String get() {
        Optional<String> env = this.env.getProperty("enc.password");
        if (env.isPresent()) {
            return env.get();
        }
        Optional<String> home = fromUserHome(user);
        if (home.isPresent()) {
            return home.get();
        }
        // TODO This shouldn't be here
        Optional<String> maven = fromMavenSettings(user, "enc.pwd", "enc.password");
        if (maven.isPresent()) {
            return maven.get();
        }
        System.err.println(format("[WARNING] password based encryption is using highly insecure default password -> '%s'", DEFAULT_ENCRYPTION_PASSWORD));
        return DEFAULT_ENCRYPTION_PASSWORD;
    }

    private Optional<String> fromUserHome(User user) {
        File file = new File(user.getHome(), "/.enc/password");
        if (!file.isFile()) {
            return absent();
        }
        try {
            return fromTrimToNull(asCharSource(file, UTF_8).readFirstLine());
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private Optional<String> fromMavenSettings(User user, String... tokens) {
        File file = new File(user.getHome(), "/.m2/settings.xml");
        if (!file.isFile()) {
            return absent();
        }
        try {
            String content = asCharSource(file, UTF_8).read();
            for (String token : tokens) {
                Optional<String> optional = fromTrimToNull(substringBetween(content, "<" + token + ">", "</" + token + ">"));
                if (optional.isPresent()) {
                    return optional;
                }
            }
            return absent();
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    public Environment getEnv() {
        return env;
    }

    public User getUser() {
        return user;
    }

}
