package org.kuali.common.jute.project;

import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.json.JsonService;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Joiner;

@JsonDeserialize(builder = MetadataRunnable.Builder.class)
public final class MetadataRunnable implements Runnable {

    private static final char FS = File.separatorChar;

    private final ProjectMetadata metadata;
    private final DirectoryContext dirs;
    private final JsonService json;

    @Override
    public void run() {
        Project project = metadata.getProject();
        File output = dirs.getMain().getOutput();
        ProjectCoordinates gav = project.getCoordinates();
        String groupId = gav.getGroupId().replace('.', FS);
        String artifactId = gav.getArtifactId();
        String filename = "metadata.json";
        String path = Joiner.on(FS).join("META-INF", groupId, artifactId, filename);
        File file = new File(output, path);
        try {
            json.write(file, metadata);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private MetadataRunnable(Builder builder) {
        this.metadata = builder.metadata;
        this.json = builder.json;
        this.dirs = builder.dirs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<MetadataRunnable>, Provider<MetadataRunnable> {

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
        public MetadataRunnable get() {
            return build();
        }

        @Override
        public MetadataRunnable build() {
            return checkNoNulls(new MetadataRunnable(this));
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
