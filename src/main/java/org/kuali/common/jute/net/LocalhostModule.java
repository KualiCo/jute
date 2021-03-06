package org.kuali.common.jute.net;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class LocalhostModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<List<NetworkInterface>>() {}).annotatedWith(NetworkInterfaces.class).toProvider(NetworkInterfacesProvider.class).asEagerSingleton();
    }

}
