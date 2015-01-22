package org.kuali.common.jute.project;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Build.Builder.class)
public final class Build {

    private final File basedir;
    private final File outputDir;
    private final File sourceDir;

    private Build(Builder builder) {
        this.basedir = builder.basedir;
        this.outputDir = builder.outputDir;
        this.sourceDir = builder.sourceDir;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Build> {

        private File basedir;
        private File outputDir;
        private File sourceDir;

        public Builder withBasedir(File basedir) {
            this.basedir = basedir;
            return this;
        }

        public Builder withOutputDir(File outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public Builder withSourceDir(File sourceDir) {
            this.sourceDir = sourceDir;
            return this;
        }

        @Override
        public Build build() {
            return checkNoNulls(new Build(this));
        }
    }

    public File getBasedir() {
        return basedir;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public File getSourceDir() {
        return sourceDir;
    }

}
