package org.kuali.common.jute.scm;

import static com.google.common.base.StandardSystemProperty.USER_HOME;
import static java.lang.Boolean.parseBoolean;
import static org.kuali.common.jute.base.Formats.getMillis;

import java.io.File;

import org.kuali.common.jute.env.Environment;
import org.kuali.common.jute.process.DefaultProcessService;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.scm.annotation.Directory;
import org.kuali.common.jute.scm.annotation.Revision;
import org.kuali.common.jute.scm.annotation.Skip;
import org.kuali.common.jute.scm.annotation.Timeout;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class SvnModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(File.class).annotatedWith(Directory.class).toInstance(new File(USER_HOME.value() + "/ws/kuali-util"));
        bindConstant().annotatedWith(Timeout.class).to(getMillis("30s"));
        bind(ProcessService.class).to(DefaultProcessService.class);
        bind(SvnRevisionProvider.class).toProvider(SvnRevisionProvider.Builder.class);
        bind(new TypeLiteral<Optional<String>>() {}).annotatedWith(Revision.class).toProvider(SvnRevisionProvider.class);
    }

    @Provides
    @Skip
    boolean skip(Environment env) {
        return parseBoolean(env.getProperty("scm.revision.skip", "false"));
    }

}
