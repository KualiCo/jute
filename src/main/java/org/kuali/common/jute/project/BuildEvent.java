package org.kuali.common.jute.project;

import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.IOException;

import org.kuali.common.jute.net.InetAddress;
import org.kuali.common.jute.system.OperatingSystem;
import org.kuali.common.jute.system.User;
import org.kuali.common.jute.system.VirtualMachine;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = BuildEvent.Builder.class)
public final class BuildEvent {

    private final String user;
    private final long timestamp;
    private final VirtualMachine vm;
    private final OperatingSystem os;
    private final InetAddress host;

    private BuildEvent(Builder builder) {
        this.user = builder.user;
        this.timestamp = builder.timestamp;
        this.vm = builder.vm;
        this.os = builder.os;
        this.host = builder.host;
    }

    public static BuildEvent build() {
        Builder builder = builder();
        builder.withVirtualMachine(VirtualMachine.build());
        builder.withOs(OperatingSystem.build());
        builder.withTimestamp(currentTimeMillis());
        builder.withUser(User.build().getName());
        try {
            builder.withHost(InetAddress.buildLocalHost());
        } catch (IOException e) {
            throw illegalState(e);
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<BuildEvent> {

        private String user;
        private long timestamp;
        private VirtualMachine vm;
        private OperatingSystem os;
        private InetAddress host;

        public Builder withHost(InetAddress host) {
            this.host = host;
            return this;
        }

        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withVirtualMachine(VirtualMachine vm) {
            this.vm = vm;
            return this;
        }

        public Builder withOs(OperatingSystem os) {
            this.os = os;
            return this;
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

}
