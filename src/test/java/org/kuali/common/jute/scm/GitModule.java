package org.kuali.common.jute.scm;

import static org.kuali.common.jute.base.Formats.getMillis;

import java.io.File;

import org.kuali.common.jute.process.DefaultProcessService;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.scm.annotation.Directory;
import org.kuali.common.jute.scm.annotation.Revision;
import org.kuali.common.jute.scm.annotation.Timeout;

import com.google.inject.AbstractModule;

public class GitModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(File.class).annotatedWith(Directory.class).toInstance(new File("/Users/jcaddel/git/kc-api"));
        bindConstant().annotatedWith(Timeout.class).to(getMillis("30s"));
        bind(ProcessService.class).to(DefaultProcessService.class);
        bind(String.class).annotatedWith(Revision.class).toProvider(GitScmProvider.class);
    }

}
