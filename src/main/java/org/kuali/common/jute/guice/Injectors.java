package org.kuali.common.jute.guice;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.inject.Guice.createInjector;

import java.util.List;

import org.kuali.common.jute.enc.openssl.OpenSSLModule;
import org.kuali.common.jute.env.EnvModule;
import org.kuali.common.jute.json.jackson.JacksonModule;
import org.kuali.common.jute.system.SystemModule;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public final class Injectors {

    private Injectors() {}

    public static List<AbstractModule> getBasicModules() {
        List<AbstractModule> list = newArrayList();
        list.add(new SystemModule());
        list.add(new EnvModule());
        list.add(new JacksonModule());
        list.add(new OpenSSLModule());
        return list;
    }

    public static Injector getBasicInjector() {
        return createInjector(getBasicModules());
    }

}
