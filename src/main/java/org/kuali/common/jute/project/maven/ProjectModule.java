package org.kuali.common.jute.project.maven;

import org.kuali.common.jute.project.BuildEvent;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ProjectModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectMetadata.class).toProvider(MetadataProvider.class);
    }

    @Provides
    protected Project project(ProjectMetadata meta) {
        return meta.getProject();
    }

    @Provides
    protected BuildEvent scm(ProjectMetadata meta) {
        return meta.getBuild();
    }

}
