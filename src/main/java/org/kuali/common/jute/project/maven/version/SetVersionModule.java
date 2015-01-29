package org.kuali.common.jute.project.maven.version;

import com.google.inject.AbstractModule;

public class SetVersionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Version.class).toProvider(VersionProvider.class);
    }

}
