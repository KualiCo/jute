package org.kuali.common.jute.project;

import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.json.JsonService;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Function;
import com.google.common.base.Joiner;

@JsonDeserialize(builder = CreateMetadataRunnable.Builder.class)
public final class CreateMetadataRunnable implements Runnable {

    private final ProjectMetadata metadata;
    private final DirectoryContext dirs;
    private final JsonService json;

    @Override
    public void run() {
        Project project = metadata.getProject();
        File output = dirs.getMain().getOutput();
        String path = MetadataPathFunction.INSTANCE.apply(project);
        File file = new File(output, path);
        try {
            json.write(file, metadata);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private enum MetadataPathFunction implements Function<Project, String> {
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

    private CreateMetadataRunnable(Builder builder) {
        this.metadata = builder.metadata;
        this.json = builder.json;
        this.dirs = builder.dirs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<CreateMetadataRunnable>, Provider<CreateMetadataRunnable> {

        private DirectoryContext dirs;
        private ProjectMetadata metadata;
        private JsonService json;

        @Inject
        public Builder withDirectoryContext(DirectoryContext dirs) {
            this.dirs = dirs;
            return this;
        }

        @Inject
        public Builder withMetadata(ProjectMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        @Inject
        public Builder withJson(JsonService json) {
            this.json = json;
            return this;
        }

        @Override
        public CreateMetadataRunnable get() {
            return build();
        }

        @Override
        public CreateMetadataRunnable build() {
            return checkNoNulls(new CreateMetadataRunnable(this));
        }
    }

    public JsonService getJson() {
        return json;
    }

    public ProjectMetadata getMetadata() {
        return metadata;
    }

    public DirectoryContext getDirs() {
        return dirs;
    }

}
