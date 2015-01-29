package org.kuali.common.jute.project.maven.version;

import org.kuali.common.jute.project.maven.annotation.Version;

import com.google.inject.AbstractModule;

public class NightlyVersionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Version.class).toProvider(NightlyVersionProvider.class);
    }

}
