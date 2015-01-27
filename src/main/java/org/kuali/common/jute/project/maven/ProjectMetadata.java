package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.project.BuildEvent;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

@JsonDeserialize(builder = ProjectMetadata.Builder.class)
public final class ProjectMetadata {

    private final Project project;
    private final BuildEvent build;
    private final Optional<DirectoryContext> dirs;

    private ProjectMetadata(Builder builder) {
        this.project = builder.project;
        this.build = builder.build;
        this.dirs = builder.dirs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProjectMetadata>, Provider<ProjectMetadata> {

        private Project project;
        private BuildEvent build;
        private Optional<DirectoryContext> dirs = absent();

        @Inject
        public Builder withProject(Project project) {
            this.project = project;
            return this;
        }

        @JsonSetter
        public Builder withDirs(Optional<DirectoryContext> dirs) {
            this.dirs = dirs;
            return this;
        }

        @Inject
        public Builder withDirs(DirectoryContext dirs) {
            return withDirs(Optional.of(dirs));
        }

        @Inject
        public Builder withBuild(BuildEvent build) {
            this.build = build;
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

    public BuildEvent getBuild() {
        return build;
    }

    public Optional<DirectoryContext> getDirs() {
        return dirs;
    }

}
