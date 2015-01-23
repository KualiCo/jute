package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.project.maven.DependencyScope.COMPILE;
import static org.kuali.common.jute.project.maven.DependencyScope.SYSTEM;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = Dependency.Builder.class)
public final class Dependency {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final DependencyScope scope;
    private final Optional<String> systemPath;
    private final ImmutableList<Exclusion> exclusions;
    private final boolean optional;
    private final String type;

    private Dependency(Builder builder) {
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
        this.version = builder.version;
        this.scope = builder.scope;
        this.systemPath = builder.systemPath;
        this.exclusions = ImmutableList.copyOf(builder.exclusions);
        this.optional = builder.optional;
        this.type = builder.type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Dependency> {

        private String groupId;
        private String artifactId;
        private String version;
        private DependencyScope scope = COMPILE;
        private Optional<String> systemPath = absent();
        private List<Exclusion> exclusions = newArrayList();
        private boolean optional = false;
        private String type = "jar";

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

        public Builder withScope(DependencyScope scope) {
            this.scope = scope;
            return this;
        }

        public Builder withSystemPath(Optional<String> systemPath) {
            this.systemPath = systemPath;
            return this;
        }

        public Builder withExclusions(List<Exclusion> exclusions) {
            this.exclusions = exclusions;
            return this;
        }

        public Builder withOptional(boolean optional) {
            this.optional = optional;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        @Override
        public Dependency build() {
            return validate(checkNoNulls(new Dependency(this)));
        }

        private static Dependency validate(Dependency instance) {
            checkNotBlank(instance.groupId, "groupId");
            checkNotBlank(instance.artifactId, "artifactId");
            checkNotBlank(instance.version, "version");
            checkNotBlank(instance.type, "type");
            if (instance.scope.equals(SYSTEM)) {
                checkNotBlank(instance.systemPath, "systemPath");
            }
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

    public DependencyScope getScope() {
        return scope;
    }

    public Optional<String> getSystemPath() {
        return systemPath;
    }

    public List<Exclusion> getExclusions() {
        return exclusions;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getType() {
        return type;
    }

}
