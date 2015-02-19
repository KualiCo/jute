package org.kuali.common.jute.kfs;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = DirPair.Builder.class)
public final class DirPair {

    private final File source;
    private final File resources;

    private DirPair(Builder builder) {
        this.source = builder.source;
        this.resources = builder.resources;
    }

    public static DirPair build(File source, File resources) {
        return builder().withResources(resources).withSource(source).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<DirPair> {

        private File source;
        private File resources;

        public Builder withSource(File source) {
            this.source = source;
            return this;
        }

        public Builder withResources(File resources) {
            this.resources = resources;
            return this;
        }

        @Override
        public DirPair build() {
            return checkNoNulls(new DirPair(this));
        }
    }

    public File getSource() {
        return source;
    }

    public File getResources() {
        return resources;
    }

}
