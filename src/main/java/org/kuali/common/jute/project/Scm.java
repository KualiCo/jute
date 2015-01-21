package org.kuali.common.jute.project;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

@JsonDeserialize(builder = Scm.Builder.class)
public final class Scm {

    private final Optional<String> connection;
    private final Optional<String> developerConnection;
    private final Optional<String> tag;
    private final Optional<String> url;

    private Scm(Builder builder) {
        this.connection = builder.connection;
        this.developerConnection = builder.developerConnection;
        this.tag = builder.tag;
        this.url = builder.url;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Scm> {

        private Optional<String> connection = absent();
        private Optional<String> developerConnection = absent();
        private Optional<String> tag = absent();
        private Optional<String> url = absent();

        public Builder withConnection(Optional<String> connection) {
            this.connection = connection;
            return this;
        }

        public Builder withDeveloperConnection(Optional<String> developerConnection) {
            this.developerConnection = developerConnection;
            return this;
        }

        public Builder withTag(Optional<String> tag) {
            this.tag = tag;
            return this;
        }

        public Builder withUrl(Optional<String> url) {
            this.url = url;
            return this;
        }

        @Override
        public Scm build() {
            return checkNoNulls(new Scm(this));
        }
    }

    public Optional<String> getConnection() {
        return connection;
    }

    public Optional<String> getDeveloperConnection() {
        return developerConnection;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public Optional<String> getUrl() {
        return url;
    }

}
