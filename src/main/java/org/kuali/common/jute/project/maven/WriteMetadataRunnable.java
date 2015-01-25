package org.kuali.common.jute.project.maven;

import static com.google.common.base.Functions.compose;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.project.maven.ProjectFunctions.metadataPathFunction;
import static org.kuali.common.jute.project.maven.ProjectFunctions.projectIdentifierFunction;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.json.JsonService;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = WriteMetadataRunnable.Builder.class)
public final class WriteMetadataRunnable implements Runnable {

    private final ProjectMetadata metadata;
    private final DirectoryContext dirs;
    private final JsonService json;

    @Override
    public void run() {
        Project project = metadata.getProject();
        File output = dirs.getMain().getOutput();
        String path = compose(metadataPathFunction(), projectIdentifierFunction()).apply(project);
        File file = new File(output, path);
        try {
            json.write(file, metadata);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private WriteMetadataRunnable(Builder builder) {
        this.metadata = builder.metadata;
        this.json = builder.json;
        this.dirs = builder.dirs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<WriteMetadataRunnable>, Provider<WriteMetadataRunnable> {

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
        public WriteMetadataRunnable get() {
            return build();
        }

        @Override
        public WriteMetadataRunnable build() {
            return checkNoNulls(new WriteMetadataRunnable(this));
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
