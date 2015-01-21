package org.kuali.common.jute.project;

import static java.util.Objects.hash;
import static org.kuali.common.jute.base.Objects.equalByComparison;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ComparisonChain;

@JsonDeserialize(builder = ProjectIdentifier.Builder.class)
public final class ProjectIdentifier implements Comparable<ProjectIdentifier> {

    private final String groupId;
    private final String artifactId;

    private ProjectIdentifier(Builder builder) {
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
    }

    public static ProjectIdentifier build(String groupId, String artifactId) {
        return builder().withGroupId(groupId).withArtifactId(artifactId).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProjectIdentifier> {

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
        public ProjectIdentifier build() {
            return validate(new ProjectIdentifier(this));
        }

        private static ProjectIdentifier validate(ProjectIdentifier instance) {
            checkNotBlank(instance.groupId, "groupId");
            checkNotBlank(instance.artifactId, "artifactId");
            return instance;
        }
    }

    @Override
    public int compareTo(ProjectIdentifier other) {
        ComparisonChain chain = ComparisonChain.start();
        chain = chain.compare(groupId, other.getGroupId());
        chain = chain.compare(artifactId, other.getArtifactId());
        return chain.result();
    }

    @Override
    public int hashCode() {
        return hash(groupId, artifactId);
    }

    @Override
    public boolean equals(Object object) {
        return equalByComparison(this, object);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

}
