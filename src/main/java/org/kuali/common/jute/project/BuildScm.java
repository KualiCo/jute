package org.kuali.common.jute.project;

import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = BuildScm.Builder.class)
public final class BuildScm {

    private final String url;
    private final String revision;

    private BuildScm(Builder builder) {
        this.url = builder.url;
        this.revision = builder.revision;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<BuildScm> {

        private String url;
        private String revision;

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
            return validate(new BuildScm(this));
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

}
