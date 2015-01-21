package org.kuali.common.jute.project;

import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Exclusion.Builder.class)
public final class Exclusion {

    private final String groupId;
    private final String artifactId;

    private Exclusion(Builder builder) {
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
    }

    public static Exclusion build(String groupId, String artifactId) {
        return builder().withGroupId(groupId).withArtifactId(artifactId).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Exclusion> {

        private String groupId;
        private String artifactId;

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withArtifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        @Override
        public Exclusion build() {
            return validate(new Exclusion(this));
        }

        public static Exclusion validate(Exclusion instance) {
            checkNotBlank(instance.groupId, "groupId");
            checkNotBlank(instance.artifactId, "artifactId");
            return instance;
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

}
