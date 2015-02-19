package org.kuali.common.jute.kfs;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = OldSrcDirs.Builder.class)
public final class OldSrcDirs {

    private final File basedir;
    private final File work;
    private final File unit;
    private final File integration;
    private final File infrastructure;

    private OldSrcDirs(Builder builder) {
        this.basedir = builder.basedir;
        this.work = builder.work;
        this.unit = builder.unit;
        this.integration = builder.integration;
        this.infrastructure = builder.infrastructure;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<OldSrcDirs> {

        private File basedir;
        private File work;
        private File unit;
        private File integration;
        private File infrastructure;

        public Builder withBasedir(File basedir) {
            this.basedir = basedir;
            return this;
        }

        public Builder withWork(File work) {
            this.work = work;
            return this;
        }

        public Builder withUnit(File unit) {
            this.unit = unit;
            return this;
        }

        public Builder withIntegration(File integration) {
            this.integration = integration;
            return this;
        }

        public Builder withInfrastructure(File infrastructure) {
            this.infrastructure = infrastructure;
            return this;
        }

        @Override
        public OldSrcDirs build() {
            return checkNoNulls(new OldSrcDirs(this));
        }
    }

    public File getBasedir() {
        return basedir;
    }

    public File getWork() {
        return work;
    }

    public File getUnit() {
        return unit;
    }

    public File getIntegration() {
        return integration;
    }

    public File getInfrastructure() {
        return infrastructure;
    }

}
