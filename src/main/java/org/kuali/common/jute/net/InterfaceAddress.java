package org.kuali.common.jute.net;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

@JsonDeserialize(builder = InterfaceAddress.Builder.class)
public final class InterfaceAddress {

    private final InetAddress address;
    private final Optional<InetAddress> broadcast;
    private final int networkPrefixLength;

    private InterfaceAddress(Builder builder) {
        this.address = builder.address;
        this.broadcast = builder.broadcast;
        this.networkPrefixLength = builder.networkPrefixLength;
    }

    public static InterfaceAddress copyOf(java.net.InterfaceAddress mutable) {
        Builder builder = builder();
        builder.withAddress(InetAddress.copyOf(mutable.getAddress()));
        if (mutable.getBroadcast() != null) {
            InetAddress broadcast = InetAddress.copyOf(mutable.getBroadcast());
            builder.withBroadcast(Optional.of(broadcast));
        }
        builder.withNetworkPrefixLength(mutable.getNetworkPrefixLength());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<InterfaceAddress> {

        private InetAddress address;
        private Optional<InetAddress> broadcast = absent();
        private int networkPrefixLength = -1;

        public Builder withNetworkPrefixLength(int networkPrefixLength) {
            this.networkPrefixLength = networkPrefixLength;
            return this;
        }

        public Builder withAddress(InetAddress address) {
            this.address = address;
            return this;
        }

        public Builder withBroadcast(Optional<InetAddress> broadcast) {
            this.broadcast = broadcast;
            return this;
        }

        @Override
        public InterfaceAddress build() {
            return validate(new InterfaceAddress(this));
        }

        private static InterfaceAddress validate(InterfaceAddress instance) {
            checkNoNulls(instance);
            checkMin(instance.networkPrefixLength, 0, "networkPrefixLength");
            return instance;
        }
    }

    public InetAddress getAddress() {
        return address;
    }

    public Optional<InetAddress> getBroadcast() {
        return broadcast;
    }

    public int getNetworkPrefixLength() {
        return networkPrefixLength;
    }

}
