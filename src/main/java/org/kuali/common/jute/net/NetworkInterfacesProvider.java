package org.kuali.common.jute.net;

import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.util.Collections.list;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import java.util.Enumeration;
import java.util.List;

import javax.inject.Provider;

import com.google.common.collect.ImmutableList;

public final class NetworkInterfacesProvider implements Provider<List<NetworkInterface>> {

    @Override
    public List<NetworkInterface> get() {
        try {
            Enumeration<java.net.NetworkInterface> e = getNetworkInterfaces();
            if (e == null) {
                return ImmutableList.of();
            } else {
                return NetworkInterface.copyOf(list(e));
            }
        } catch (Exception e) {
            throw illegalState(e);
        }
    }

}
