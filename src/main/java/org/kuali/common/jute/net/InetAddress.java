package org.kuali.common.jute.net;

import static com.google.common.collect.Lists.newArrayList;
import static java.net.InetAddress.getLocalHost;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = InetAddress.Builder.class)
public final class InetAddress {

    private final String hostName;
    private final String canonicalHostName;
    private final String hostAddress;

    private InetAddress(Builder builder) {
        this.canonicalHostName = builder.canonicalHostName;
        this.hostName = builder.hostName;
        this.hostAddress = builder.hostAddress;
    }

    public static InetAddress buildLocalHost() throws IOException {
        return copyOf(getLocalHost());
    }

    public static List<InetAddress> copyOf(Iterable<java.net.InetAddress> mutables) {
        List<InetAddress> list = newArrayList();
        for (java.net.InetAddress mutable : mutables) {
            list.add(copyOf(mutable));
        }
        return ImmutableList.copyOf(list);
    }

    public static InetAddress copyOf(java.net.InetAddress mutable) {
        Builder builder = builder();
        builder.withCanonicalHostName(mutable.getCanonicalHostName());
        builder.withHostAddress(mutable.getHostAddress());
        builder.withHostName(mutable.getHostName());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<InetAddress> {

        private String canonicalHostName;
        private String hostName;
        private String hostAddress;

        public Builder withCanonicalHostName(String canonicalHostName) {
            this.canonicalHostName = canonicalHostName;
            return this;
        }

        public Builder withHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder withHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        @Override
        public InetAddress build() {
            return validate(new InetAddress(this));
        }

        private static InetAddress validate(InetAddress instance) {
            checkNotBlank(instance.hostName, "hostName");
            checkNotBlank(instance.canonicalHostName, "canonicalHostName");
            checkNotBlank(instance.hostAddress, "hostAddress");
            return instance;
        }
    }

    public String getCanonicalHostName() {
        return canonicalHostName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

}
