package org.kuali.common.jute.project;

import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import org.kuali.common.jute.scm.ScmType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = BuildScm.Builder.class)
public final class BuildScm {

    private final String url;
    private final String revision;
    private final ScmType type;

    private BuildScm(Builder builder) {
        this.url = builder.url;
        this.revision = builder.revision;
        this.type = builder.type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<BuildScm> {

        private String url;
        private String revision;
        private ScmType type;

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withRevision(String revision) {
            this.revision = revision;
            return this;
        }

        @Override
        public BuildScm build() {
            return validate(checkNoNulls(new BuildScm(this)));
        }

        private static final BuildScm validate(BuildScm instance) {
            checkNotBlank(instance.url, "url");
            checkNotBlank(instance.revision, "revision");
            return instance;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getRevision() {
        return revision;
    }

    public ScmType getType() {
        return type;
    }

}
