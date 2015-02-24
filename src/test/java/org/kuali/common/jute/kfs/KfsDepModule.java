package org.kuali.common.jute.kfs;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Files.readLines;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.Deps;
import org.kuali.common.jute.kfs.annotation.PomContent;
import org.kuali.common.jute.system.User;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class KfsDepModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<List<String>>() {}).annotatedWith(Deps.class).toProvider(DepsProvider.class);
        bind(new TypeLiteral<List<List<String>>>() {}).annotatedWith(Deps.class).toProvider(DepsContainerProvider.class);
    }

    @Provides
    @Basedir
    protected File basedir(Environment env, User user) {
        try {
            String basedir = env.getProperty("kfs.basedir", user.getHome() + "/git/kfs");
            return new File(basedir).getCanonicalFile();
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    @Provides
    @PomContent
    protected List<String> pomContent(@Basedir File basedir) {
        try {
            File pom = new File(basedir, "pom.xml");
            return readLines(pom, UTF_8);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

}
