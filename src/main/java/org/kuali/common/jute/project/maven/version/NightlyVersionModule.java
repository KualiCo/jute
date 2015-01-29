package org.kuali.common.jute.project.maven.version;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.process.ProcessRunnable;
import org.kuali.common.jute.project.maven.annotation.Version;

import com.google.inject.AbstractModule;

public class NightlyVersionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Version.class).toProvider(NightlyVersionProvider.class);
        bind(ProcessContext.class).toProvider(SetVersionProcessContextProvider.class);
        bind(Runnable.class).toProvider(ProcessRunnable.Builder.class);
    }

}
