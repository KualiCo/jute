package org.kuali.common.jute.ant;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static org.kuali.common.jute.base.Objects.equalByComparison;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = Target.Builder.class)
public final class Target implements Comparable<Target> {

    private final String name;
    private final ImmutableList<String> depends;
    private final Optional<String> unless;
    private final Optional<String> iff;

    private Target(Builder builder) {
        this.name = builder.name;
        this.unless = builder.unless;
        this.iff = builder.iff;
        this.depends = ImmutableList.copyOf(builder.depends);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Target> {

        private String name;
        private List<String> depends = newArrayList();
        private Optional<String> unless = absent();
        private Optional<String> iff = absent();

        public Builder withIff(Optional<String> iff) {
            this.iff = iff;
            return this;
        }

        public Builder withUnless(Optional<String> unless) {
            this.unless = unless;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDepends(List<String> depends) {
            this.depends = depends;
            return this;
        }

        @Override
        public Target build() {
            return checkNoNulls(new Target(this));
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getDepends() {
        return depends;
    }

    @Override
    public int compareTo(Target other) {
        return name.compareTo(other.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return equalByComparison(this, object);
    }

    @Override
    public String toString() {
        return name;
    }

    public Optional<String> getUnless() {
        return unless;
    }

    public Optional<String> getIff() {
        return iff;
    }

}
