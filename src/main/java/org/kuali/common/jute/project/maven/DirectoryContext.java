package org.kuali.common.jute.project.maven;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = DirectoryContext.Builder.class)
public final class DirectoryContext {

    private final File basedir;
    private final DirectoryPair main;
    private final DirectoryPair test;

    private DirectoryContext(Builder builder) {
        this.basedir = builder.basedir;
        this.main = builder.main;
        this.test = builder.test;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<DirectoryContext> {

        private File basedir;
        private DirectoryPair main;
        private DirectoryPair test;

        public Builder withBasedir(File basedir) {
            this.basedir = basedir;
            return this;
        }

        public Builder withMain(DirectoryPair main) {
            this.main = main;
            return this;
        }

        public Builder withTest(DirectoryPair test) {
            this.test = test;
            return this;
        }

        @Override
        public DirectoryContext build() {
            return checkNoNulls(new DirectoryContext(this));
        }
    }

    public File getBasedir() {
        return basedir;
    }

    public DirectoryPair getMain() {
        return main;
    }

    public DirectoryPair getTest() {
        return test;
    }

}
