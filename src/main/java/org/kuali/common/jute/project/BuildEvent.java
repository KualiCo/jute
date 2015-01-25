package org.kuali.common.jute.project;

import static com.google.common.base.Optional.absent;
import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.net.InetAddress;
import org.kuali.common.jute.project.annotation.BuildHost;
import org.kuali.common.jute.project.annotation.BuildRevision;
import org.kuali.common.jute.project.annotation.BuildTimestamp;
import org.kuali.common.jute.system.Java;
import org.kuali.common.jute.system.OperatingSystem;
import org.kuali.common.jute.system.RuntimeEnvironment;
import org.kuali.common.jute.system.User;
import org.kuali.common.jute.system.VirtualMachine;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

@JsonDeserialize(builder = BuildEvent.Builder.class)
public final class BuildEvent {

    private final String user;
    private final long timestamp;
    private final Optional<BuildScm> scm;
    private final VirtualMachine vm;
    private final RuntimeEnvironment runtime;
    private final OperatingSystem os;
    private final InetAddress host;

    private BuildEvent(Builder builder) {
        this.user = builder.user;
        this.timestamp = builder.timestamp;
        this.vm = builder.vm;
        this.runtime = builder.runtime;
        this.os = builder.os;
        this.host = builder.host;
        this.scm = builder.scm;
    }

    public static BuildEvent build() throws IOException {
        Builder builder = builder();
        builder.withVm(VirtualMachine.build());
        builder.withOs(OperatingSystem.build());
        builder.withTimestamp(currentTimeMillis());
        builder.withUser(User.build().getName());
        builder.withHost(InetAddress.buildLocalHost());
        builder.withRuntime(RuntimeEnvironment.build());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<BuildEvent>, Provider<BuildEvent> {

        private long timestamp;
        private InetAddress host;
        private String user;
        private VirtualMachine vm;
        private OperatingSystem os;
        private RuntimeEnvironment runtime;
        private Optional<BuildScm> scm = absent();

        @Inject
        public Builder withScm(@BuildRevision Optional<BuildScm> scm) {
            this.scm = scm;
            return this;
        }

        @Inject
        public Builder withJava(Java java) {
            withRuntime(java.getRuntime());
            withVm(java.getVm());
            return this;
        }

        @Inject
        public Builder withHost(@BuildHost InetAddress host) {
            this.host = host;
            return this;
        }

        @Inject
        public Builder withTimestamp(@BuildTimestamp long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Inject
        public Builder withOs(OperatingSystem os) {
            this.os = os;
            return this;
        }

        @Inject
        public Builder withUser(User user) {
            return withUser(user.getName());
        }

        @JsonSetter
        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public Builder withRuntime(RuntimeEnvironment runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder withVm(VirtualMachine vm) {
            this.vm = vm;
            return this;
        }

        @Override
        public BuildEvent get() {
            return build();
        }

        @Override
        public BuildEvent build() {
            return checkNoNulls(new BuildEvent(this));
        }
    }

    public String getUser() {
        return user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public VirtualMachine getVm() {
        return vm;
    }

    public OperatingSystem getOs() {
        return os;
    }

    public InetAddress getHost() {
        return host;
    }

    public RuntimeEnvironment getRuntime() {
        return runtime;
    }

    public Optional<BuildScm> getScm() {
        return scm;
    }

}
