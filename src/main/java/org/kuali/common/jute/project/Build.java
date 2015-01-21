package org.kuali.common.jute.project;

import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import org.kuali.common.jute.system.Java;
import org.kuali.common.jute.system.OperatingSystem;
import org.kuali.common.jute.system.User;

public final class Build {

    private final String username;
    private final long timestamp;
    private final Java java;
    private final OperatingSystem os;

    private Build(Builder builder) {
        this.username = builder.username;
        this.timestamp = builder.timestamp;
        this.java = builder.java;
        this.os = builder.os;
    }

    public static Build build() {
        Builder builder = builder();
        builder.withJava(Java.build());
        builder.withOs(OperatingSystem.build());
        builder.withTimestamp(currentTimeMillis());
        builder.withUsername(User.build().getName());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Build> {

        private String username;
        private long timestamp;
        private Java java;
        private OperatingSystem os;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withJava(Java java) {
            this.java = java;
            return this;
        }

        public Builder withOs(OperatingSystem os) {
            this.os = os;
            return this;
        }

        @Override
        public Build build() {
            return checkNoNulls(new Build(this));
        }
    }

    public String getUsername() {
        return username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Java getJava() {
        return java;
    }

    public OperatingSystem getOs() {
        return os;
    }

}
