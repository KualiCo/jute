package org.kuali.common.jute.net;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.io.BaseEncoding.base16;
import static java.util.Collections.list;
import static org.kuali.common.jute.base.Optionals.fromNegativeToAbsent;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;

@JsonDeserialize(builder = NetworkInterface.Builder.class)
public final class NetworkInterface {

    private final Optional<NetworkInterface> parent;
    private final String name;
    private final String displayName;
    private final ImmutableList<InetAddress> addresses;
    private final ImmutableList<NetworkInterface> subInterfaces;
    private final ImmutableList<InterfaceAddress> interfaceAddresses;
    private final Optional<Integer> index;
    private final int mtu;
    private final boolean loopback;
    private final boolean pointToPoint;
    private final boolean up;
    private final boolean virtual;
    private final boolean supportsMulticast;
    private final Optional<String> hardwareAddress;

    public static List<NetworkInterface> copyOf(Iterable<java.net.NetworkInterface> mutables) throws IOException {
        List<NetworkInterface> list = newArrayList();
        for (java.net.NetworkInterface mutable : mutables) {
            list.add(copyOf(mutable));
        }
        return ImmutableList.copyOf(list);
    }

    public static NetworkInterface copyOf(java.net.NetworkInterface mutable) throws IOException {
        Builder builder = builder();
        if (mutable.getParent() != null) {
            builder.withParent(Optional.of(copyOf(mutable.getParent())));
        }
        builder.withName(mutable.getName());
        builder.withAddresses(InetAddress.copyOf(list(mutable.getInetAddresses())));
        builder.withDisplayName(mutable.getDisplayName());
        builder.withIndex(fromNegativeToAbsent(mutable.getIndex()));
        builder.withMtu(mutable.getMTU());
        builder.withLoopback(mutable.isLoopback());
        builder.withPointToPoint(mutable.isPointToPoint());
        builder.withUp(mutable.isUp());
        builder.withVirtual(mutable.isVirtual());
        builder.withSupportsMulticast(mutable.supportsMulticast());
        builder.withHardwareAddress(getHardwareAddress(mutable.getHardwareAddress()));
        builder.withSubInterfaces(copyOf(list(mutable.getSubInterfaces())));
        return builder.build();
    }

    public static Optional<String> getHardwareAddress(byte[] address) {
        if (address == null) {
            return absent();
        }
        BaseEncoding encoder = base16().lowerCase();
        List<String> lines = newArrayList();
        for (int i = 0; i < address.length; i++) {
            lines.add(encoder.encode(address, i, 1));
        }
        return Optional.of(Joiner.on(':').join(lines));
    }

    public static Builder builder() {
        return new Builder();
    }

    private NetworkInterface(Builder builder) {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.addresses = ImmutableList.copyOf(builder.addresses);
        this.index = builder.index;
        this.mtu = builder.mtu;
        this.loopback = builder.loopback;
        this.pointToPoint = builder.pointToPoint;
        this.up = builder.up;
        this.virtual = builder.virtual;
        this.supportsMulticast = builder.supportsMulticast;
        this.hardwareAddress = builder.hardwareAddress;
        this.parent = builder.parent;
        this.subInterfaces = ImmutableList.copyOf(builder.subInterfaces);
        this.interfaceAddresses = ImmutableList.copyOf(builder.interfaceAddresses);
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<NetworkInterface> {

        private Optional<NetworkInterface> parent = absent();
        private String name;
        private String displayName;
        private Optional<Integer> index = absent();
        private int mtu = -1;
        private List<InetAddress> addresses = newArrayList();
        private boolean loopback;
        private boolean pointToPoint;
        private boolean up;
        private boolean virtual;
        private boolean supportsMulticast;
        private Optional<String> hardwareAddress = absent();
        private List<NetworkInterface> subInterfaces = newArrayList();
        private List<InterfaceAddress> interfaceAddresses = newArrayList();

        public Builder withInterfaceAddresses(List<InterfaceAddress> interfaceAddresses) {
            this.interfaceAddresses = interfaceAddresses;
            return this;
        }

        public Builder withSubInterfaces(List<NetworkInterface> subInterfaces) {
            this.subInterfaces = subInterfaces;
            return this;
        }

        public Builder withParent(Optional<NetworkInterface> parent) {
            this.parent = parent;
            return this;
        }

        public Builder withHardwareAddress(Optional<String> hardwareAddress) {
            this.hardwareAddress = hardwareAddress;
            return this;
        }

        public Builder withSupportsMulticast(boolean supportsMulticast) {
            this.supportsMulticast = supportsMulticast;
            return this;
        }

        public Builder withVirtual(boolean virtual) {
            this.virtual = virtual;
            return this;
        }

        public Builder withUp(boolean up) {
            this.up = up;
            return this;
        }

        public Builder withPointToPoint(boolean pointToPoint) {
            this.pointToPoint = pointToPoint;
            return this;
        }

        public Builder withLoopback(boolean loopback) {
            this.loopback = loopback;
            return this;
        }

        public Builder withMtu(int mtu) {
            this.mtu = mtu;
            return this;
        }

        public Builder withIndex(Optional<Integer> index) {
            this.index = index;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withAddresses(List<InetAddress> addresses) {
            this.addresses = addresses;
            return this;
        }

        @Override
        public NetworkInterface build() {
            return validate(new NetworkInterface(this));
        }

        private static NetworkInterface validate(NetworkInterface instance) {
            checkNoNulls(instance);
            checkNotBlank(instance.name, "name");
            checkNotBlank(instance.displayName, "displayName");
            checkNotBlank(instance.hardwareAddress, "hardwareAddress");
            checkMin(instance.index, 0, "index");
            checkMin(instance.mtu, 0, "mtu");
            return instance;
        }
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<InetAddress> getAddresses() {
        return addresses;
    }

    public Optional<Integer> getIndex() {
        return index;
    }

    public int getMtu() {
        return mtu;
    }

    public boolean isLoopback() {
        return loopback;
    }

    public boolean isPointToPoint() {
        return pointToPoint;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public boolean isSupportsMulticast() {
        return supportsMulticast;
    }

    public Optional<String> getHardwareAddress() {
        return hardwareAddress;
    }

    public Optional<NetworkInterface> getParent() {
        return parent;
    }

    public List<NetworkInterface> getSubInterfaces() {
        return subInterfaces;
    }

    public List<InterfaceAddress> getInterfaceAddresses() {
        return interfaceAddresses;
    }

}
