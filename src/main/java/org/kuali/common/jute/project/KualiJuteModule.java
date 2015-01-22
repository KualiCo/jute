package org.kuali.common.jute.project;

import org.kuali.common.jute.json.JsonService;
import org.kuali.common.jute.project.annotation.KualiJuteProjectId;
import org.kuali.common.jute.project.annotation.KualiJuteProjectMetadata;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class KualiJuteModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ProjectIdentifier.class).annotatedWith(KualiJuteProjectId.class).toInstance(ProjectIdentifier.build("org.kuali.common", "kuali-jute"));
    }

    @Provides
    @KualiJuteProjectMetadata
    protected ProjectMetadata projectMetadata(@KualiJuteProjectId ProjectIdentifier pid, JsonService json) {
        return ProjectMetadataProvider.get(pid, json);
    }

}
