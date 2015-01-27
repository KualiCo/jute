package org.kuali.common.jute.project.maven;

import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.project.maven.annotation.KualiJuteProjectId;
import org.kuali.common.jute.project.maven.annotation.KualiJuteProjectMetadata;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class KualiJuteModule extends AbstractModule {

    @Override
    public void configure() {
        // the groupId/artifactId must *exactly* match what is in the pom
        bind(ProjectIdentifier.class).annotatedWith(KualiJuteProjectId.class).toInstance(ProjectIdentifier.build("org.kuali.common", "kuali-jute"));
    }

    @Provides
    @KualiJuteProjectMetadata
    protected ProjectMetadata projectMetadata(@KualiJuteProjectId ProjectIdentifier pid, JsonService json) {
        return MetadataProvider.get(pid, json);
    }

}
