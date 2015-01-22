package org.kuali.common.jute.project;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = DirectoryPair.Builder.class)
public final class DirectoryPair {

    private final File source;
    private final File output;

    private DirectoryPair(Builder builder) {
        this.source = builder.source;
        this.output = builder.output;
    }

    public static DirectoryPair build(File source, File output) {
        return builder().withSource(source).withOutput(output).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<DirectoryPair> {

        private File source;
        private File output;

        public Builder withSource(File source) {
            this.source = source;
            return this;
        }

        public Builder withOutput(File output) {
            this.output = output;
            return this;
        }

        @Override
        public DirectoryPair build() {
            return checkNoNulls(new DirectoryPair(this));
        }
    }

    public File getSource() {
        return source;
    }

    public File getOutput() {
        return output;
    }

}
