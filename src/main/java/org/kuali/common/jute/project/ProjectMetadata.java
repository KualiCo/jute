package org.kuali.common.jute.project;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProjectMetadata.Builder.class)
public final class ProjectMetadata {

    private final Project project;
    private final BuildEvent event;

    private ProjectMetadata(Builder builder) {
        this.project = builder.project;
        this.event = builder.event;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProjectMetadata>, Provider<ProjectMetadata> {

        private Project project;
        private BuildEvent event;

        @Inject
        public Builder withProject(Project project) {
            this.project = project;
            return this;
        }

        @Inject
        public Builder withEvent(BuildEvent event) {
            this.event = event;
            return this;
        }

        @Override
        public ProjectMetadata get() {
            return build();
        }

        @Override
        public ProjectMetadata build() {
            return checkNoNulls(new ProjectMetadata(this));
        }
    }

    public Project getProject() {
        return project;
    }

    public BuildEvent getEvent() {
        return event;
    }

}
