package org.kuali.common.jute.project.maven;

import com.google.inject.AbstractModule;

public class KualiJuteModule extends AbstractModule {

    @Override
    public void configure() {
        // the groupId/artifactId must *exactly* match what is in the pom
        bind(ProjectIdentifier.class).toInstance(ProjectIdentifier.build("org.kuali.common", "kuali-jute"));
    }

}
