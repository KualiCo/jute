package org.kuali.common.jute.project.maven;

import com.google.inject.AbstractModule;

public class MetadataModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectMetadata.class).toProvider(MetadataProvider.class);
    }

}
