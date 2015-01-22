package org.kuali.common.jute.project;

import com.google.common.base.Function;

public enum ProjectIdentifierFunction implements Function<Project, ProjectIdentifier> {
    INSTANCE;

    @Override
    public ProjectIdentifier apply(Project input) {
        ProjectCoordinates gav = input.getCoordinates();
        return ProjectIdentifier.build(gav.getGroupId(), gav.getArtifactId());
    }
}
