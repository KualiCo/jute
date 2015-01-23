package org.kuali.common.jute.project.maven;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public final class ProjectFunctions {

    private ProjectFunctions() {}

    public static Function<Project, ProjectIdentifier> projectIdentifierFunction() {
        return ProjectIdentifierFunction.INSTANCE;
    }

    private enum ProjectIdentifierFunction implements Function<Project, ProjectIdentifier> {
        INSTANCE;

        @Override
        public ProjectIdentifier apply(Project input) {
            ProjectCoordinates gav = input.getCoordinates();
            return ProjectIdentifier.build(gav.getGroupId(), gav.getArtifactId());
        }
    }

    public static Function<ProjectIdentifier, String> metadataPathFunction() {
        return MetadataPathFunction.INSTANCE;
    }

    private enum MetadataPathFunction implements Function<ProjectIdentifier, String> {
        INSTANCE;

        @Override
        public String apply(ProjectIdentifier input) {
            String groupId = input.getGroupId().replace('.', '/');
            String artifactId = input.getArtifactId();
            String filename = "metadata.json";
            return Joiner.on('/').join("META-INF", groupId, artifactId, filename);
        }
    }

}
