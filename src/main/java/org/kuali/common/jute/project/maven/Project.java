package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.hash;
import static org.kuali.common.jute.base.Objects.equalByComparison;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;
import java.util.Properties;

import org.kuali.common.jute.collect.ImmutableProperties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = Project.Builder.class)
public final class Project implements Comparable<Project> {

    private final Optional<ProjectCoordinates> parent;
    private final ProjectCoordinates coordinates;
    private final String packaging;
    private final String encoding;
    private final Optional<MavenScm> scm;
    private final Optional<String> name;
    private final Optional<String> description;
    private final Optional<String> url;
    private final Optional<String> inceptionYear;
    private final ImmutableList<Dependency> dependencies;
    private final ImmutableList<License> licenses;
    private final ImmutableProperties properties;

    private Project(Builder builder) {
        this.parent = builder.parent;
        this.coordinates = builder.coordinates;
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

        private Optional<ProjectCoordinates> parent = absent();
        private ProjectCoordinates coordinates;
        private String encoding;
        private String packaging = "jar";
        private Optional<MavenScm> scm = absent();
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

        public Builder withCoordinates(ProjectCoordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder withParent(Optional<ProjectCoordinates> parent) {
            this.parent = parent;
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

        public Builder withScm(Optional<MavenScm> scm) {
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
            checkNotBlank(instance.packaging, "packaging");
            return instance;
        }
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

    public Optional<MavenScm> getScm() {
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

    public Optional<ProjectCoordinates> getParent() {
        return parent;
    }

    public ProjectCoordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public int compareTo(Project other) {
        return coordinates.compareTo(other.getCoordinates());
    }

    @Override
    public int hashCode() {
        return hash(coordinates);
    }

    @Override
    public boolean equals(Object other) {
        return equalByComparison(this, other);
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }

}
