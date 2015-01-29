package org.kuali.common.jute.project.maven.version;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Optional.absent;
import static java.util.Objects.hash;
import static org.kuali.common.jute.base.Objects.equalByComparison;
import static org.kuali.common.jute.base.Orderings.NULLS_FIRST;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;
import com.google.common.collect.ComparisonChain;

@JsonDeserialize(builder = Version.Builder.class)
public final class Version implements Comparable<Version> {

    private final int major;
    private final int minor;
    private final int patch;
    private final Optional<String> qualifier;
    private final boolean snapshot;

    private Version(Builder builder) {
        this.major = builder.major;
        this.minor = builder.minor;
        this.patch = builder.patch;
        this.qualifier = builder.qualifier;
        this.snapshot = builder.snapshot;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Version> {

        private int major;
        private int minor;
        private int patch;
        private Optional<String> qualifier = absent();
        private boolean snapshot = false;

        public Builder withMajor(int major) {
            this.major = major;
            return this;
        }

        public Builder withMinor(int minor) {
            this.minor = minor;
            return this;
        }

        public Builder withPatch(int patch) {
            this.patch = patch;
            return this;
        }

        public Builder withQualifier(Optional<String> qualifier) {
            this.qualifier = qualifier;
            return this;
        }

        public Builder withSnapshot(boolean snapshot) {
            this.snapshot = snapshot;
            return this;
        }

        @Override
        public Version build() {
            return validate(new Version(this));
        }

        private static final Version validate(Version instance) {
            checkMin(instance.major, 0, "major");
            checkMin(instance.minor, 0, "minor");
            checkMin(instance.patch, 0, "patch");
            checkNotBlank(instance.qualifier, "qualifier");
            return instance;
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public Optional<String> getQualifier() {
        return qualifier;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    @Override
    public int compareTo(Version other) {
        ComparisonChain chain = ComparisonChain.start();
        chain.compare(major, other.major);
        chain.compare(minor, other.minor);
        chain.compare(patch, other.patch);
        chain.compare(qualifier.orNull(), other.qualifier.orNull(), NULLS_FIRST);
        chain.compare(snapshot, other.snapshot);
        return chain.result();
    }

    @Override
    public int hashCode() {
        return hash(major, minor, patch, qualifier.orNull(), snapshot);
    }

    @Override
    public boolean equals(Object other) {
        return equalByComparison(this, other);
    }

    @Override
    public String toString() {
        ToStringHelper h = toStringHelper(this);
        h.add("major", major);
        h.add("minor", minor);
        h.add("patch", patch);
        h.add("qualifier", qualifier.orNull());
        h.add("snapshot", snapshot);
        return h.toString();
    }

}
