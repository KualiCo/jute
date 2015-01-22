package org.kuali.common.jute.project;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;
import java.util.Properties;

import org.kuali.common.jute.collect.ImmutableProperties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = Project.Builder.class)
public final class Project {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String packaging;
    private final String encoding;
    private final Optional<Scm> scm;
    private final Optional<String> name;
    private final Optional<String> description;
    private final Optional<String> url;
    private final Optional<String> inceptionYear;
    private final ImmutableList<Dependency> dependencies;
    private final ImmutableList<License> licenses;
    @JsonIgnore
    private final ImmutableProperties properties;

    private Project(Builder builder) {
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
        this.version = builder.version;
        this.name = builder.name;
        this.description = builder.description;
        this.url = builder.url;
        this.dependencies = ImmutableList.copyOf(builder.dependencies);
        this.licenses = ImmutableList.copyOf(builder.licenses);
        this.scm = builder.scm;
        this.properties = ImmutableProperties.copyOf(builder.properties);
        this.inceptionYear = builder.inceptionYear;
        this.packaging = builder.packaging;
        this.encoding = builder.encoding;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Project> {

        private String groupId;
        private String artifactId;
        private String version;
        private String encoding;
        private String packaging = "jar";
        private Optional<Scm> scm = absent();
        private Optional<String> name = absent();
        private Optional<String> description = absent();
        private Optional<String> url = absent();
        private List<Dependency> dependencies = newArrayList();
        private List<License> licenses = newArrayList();
        private Properties properties = new Properties();
        private Optional<String> inceptionYear = absent();

        public Builder withEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

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

        public Builder withName(Optional<String> name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(Optional<String> description) {
            this.description = description;
            return this;
        }

        public Builder withUrl(Optional<String> url) {
            this.url = url;
            return this;
        }

        public Builder withDependencies(List<Dependency> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public Builder withLicenses(List<License> licenses) {
            this.licenses = licenses;
            return this;
        }

        public Builder withScm(Optional<Scm> scm) {
            this.scm = scm;
            return this;
        }

        public Builder withProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public Builder withInceptionYear(Optional<String> inceptionYear) {
            this.inceptionYear = inceptionYear;
            return this;
        }

        public Builder withPackaging(String packaging) {
            this.packaging = packaging;
            return this;
        }

        @Override
        public Project build() {
            return validate(checkNoNulls(new Project(this)));
        }

        private static Project validate(Project instance) {
            checkNotBlank(instance.groupId, "groupId");
            checkNotBlank(instance.artifactId, "artifactId");
            checkNotBlank(instance.version, "version");
            checkNotBlank(instance.packaging, "packaging");
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

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<String> getUrl() {
        return url;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public Optional<Scm> getScm() {
        return scm;
    }

    public Properties getProperties() {
        return properties;
    }

    public Optional<String> getInceptionYear() {
        return inceptionYear;
    }

    public String getPackaging() {
        return packaging;
    }

    public String getEncoding() {
        return encoding;
    }

}
