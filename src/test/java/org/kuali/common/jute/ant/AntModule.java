package org.kuali.common.jute.ant;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Files.asCharSource;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.kuali.common.jute.ant.annotation.BuildFile;
import org.kuali.common.jute.ant.annotation.BuildFileContent;
import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.system.User;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class AntModule extends AbstractModule {

    @Provides
    @BuildFile
    protected File buildFile(Environment env, User user) {
        File checkout = new File(env.getProperty("kfs.checkout", user.getHome().getAbsolutePath() + "/git/kfs"));
        return new File(checkout, "build.xml");
    }

    @Provides
    @BuildFileContent
    protected String buildFileContent(@BuildFile File buildFile) {
        try {
            return asCharSource(buildFile, UTF_8).read();
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    @Override
    protected void configure() {
        bind(new TypeLiteral<List<Target>>() {}).toProvider(TargetTokensProvider.class);
    }

}
