package org.kuali.common.jute.project.maven;

import static java.util.Objects.hash;
import static org.kuali.common.jute.base.Objects.equalByComparison;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Joiner;
import com.google.common.collect.ComparisonChain;

@JsonDeserialize(builder = ProjectCoordinates.Builder.class)
public final class ProjectCoordinates implements Comparable<ProjectCoordinates> {

    private final String groupId;
    private final String artifactId;
    private final String version;

    private ProjectCoordinates(Builder builder) {
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
        this.version = builder.version;
    }

    public static ProjectCoordinates build(String groupId, String artifactId, String version) {
        Builder builder = builder();
        builder.withGroupId(groupId);
        builder.withArtifactId(artifactId);
        builder.withVersion(version);
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProjectCoordinates> {

        private String groupId;
        private String artifactId;
        private String version;

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withArtifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        @Override
        public ProjectCoordinates build() {
            return validate(new ProjectCoordinates(this));
        }

        private static ProjectCoordinates validate(ProjectCoordinates instance) {
            checkNotBlank(instance.groupId, "groupId");
            checkNotBlank(instance.artifactId, "artifactId");
            checkNotBlank(instance.version, "version");
            return instance;
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int compareTo(ProjectCoordinates other) {
        ComparisonChain chain = ComparisonChain.start();
        chain.compare(groupId, other.groupId);
        chain.compare(artifactId, other.artifactId);
        chain.compare(version, other.version);
        return chain.result();
    }

    @Override
    public int hashCode() {
        return hash(groupId, artifactId, version);
    }

    @Override
    public boolean equals(Object other) {
        return equalByComparison(this, other);
    }

    @Override
    public String toString() {
        return Joiner.on(':').join(groupId, artifactId, version);
    }

}
