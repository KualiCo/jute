package org.kuali.common.jute.project;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public enum MetadataPathFunction implements Function<Project, String> {
    INSTANCE;

    @Override
    public String apply(Project input) {
        ProjectCoordinates gav = input.getCoordinates();
        String groupId = gav.getGroupId().replace('.', '/');
        String artifactId = gav.getArtifactId();
        String filename = "metadata.json";
        return Joiner.on('/').join("META-INF", groupId, artifactId, filename);
    }
}
