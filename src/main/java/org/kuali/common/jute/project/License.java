package org.kuali.common.jute.project;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

@JsonDeserialize(builder = License.Builder.class)
public final class License {

    private final Optional<String> name;
    private final Optional<String> url;
    private final Optional<String> comments;

    private License(Builder builder) {
        this.name = builder.name;
        this.url = builder.url;
        this.comments = builder.comments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<License> {

        private Optional<String> name = absent();
        private Optional<String> url = absent();
        private Optional<String> comments = absent();

        public Builder withName(Optional<String> name) {
            this.name = name;
            return this;
        }

        public Builder withUrl(Optional<String> url) {
            this.url = url;
            return this;
        }

        public Builder withComments(Optional<String> comments) {
            this.comments = comments;
            return this;
        }

        @Override
        public License build() {
            return checkNoNulls(new License(this));
        }
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getUrl() {
        return url;
    }

    public Optional<String> getComments() {
        return comments;
    }

}
