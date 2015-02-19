package org.kuali.common.jute.kfs;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = NewSrcDirs.Builder.class)
public final class NewSrcDirs {

    private final File basedir;
    private final DirPair main;
    private final DirPair test;
    private final DirPair integration;
    private final DirPair infrastructure;
    private final File webapp;

    private NewSrcDirs(Builder builder) {
        this.basedir = builder.basedir;
        this.main = builder.main;
        this.test = builder.test;
        this.integration = builder.integration;
        this.infrastructure = builder.infrastructure;
        this.webapp = builder.webapp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<NewSrcDirs> {

        private File basedir;
        private DirPair main;
        private DirPair test;
        private DirPair integration;
        private DirPair infrastructure;
        private File webapp;

        public Builder withWebapp(File webapp) {
            this.webapp = webapp;
            return this;
        }

        public Builder withBasedir(File basedir) {
            this.basedir = basedir;
            return this;
        }

        public Builder withIntegration(DirPair integration) {
            this.integration = integration;
            return this;
        }

        public Builder withInfrastructure(DirPair infrastructure) {
            this.infrastructure = infrastructure;
            return this;
        }

        public Builder withMain(DirPair main) {
            this.main = main;
            return this;
        }

        public Builder withTest(DirPair test) {
            this.test = test;
            return this;
        }

        @Override
        public NewSrcDirs build() {
            return checkNoNulls(new NewSrcDirs(this));
        }
    }

    public File getBasedir() {
        return basedir;
    }

    public DirPair getMain() {
        return main;
    }

    public DirPair getTest() {
        return test;
    }

    public DirPair getIntegration() {
        return integration;
    }

    public DirPair getInfrastructure() {
        return infrastructure;
    }

    public File getWebapp() {
        return webapp;
    }

}
