package org.kuali.common.jute.project;

import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.IOException;

import org.kuali.common.jute.net.InetAddress;
import org.kuali.common.jute.system.Java;
import org.kuali.common.jute.system.OperatingSystem;
import org.kuali.common.jute.system.User;

public final class Build {

    private final String username;
    private final long timestamp;
    private final Java java;
    private final OperatingSystem os;
    private final InetAddress host;

    private Build(Builder builder) {
        this.username = builder.username;
        this.timestamp = builder.timestamp;
        this.java = builder.java;
        this.os = builder.os;
        this.host = builder.host;
    }

    public static Build build() throws IOException {
        Builder builder = builder();
        builder.withJava(Java.build());
        builder.withOs(OperatingSystem.build());
        builder.withTimestamp(currentTimeMillis());
        builder.withUsername(User.build().getName());
        builder.withHost(InetAddress.buildLocalHost());
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
        private InetAddress host;

        public Builder withHost(InetAddress host) {
            this.host = host;
            return this;
        }

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

    public InetAddress getHost() {
        return host;
    }

}
