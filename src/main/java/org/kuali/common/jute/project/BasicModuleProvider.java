package org.kuali.common.jute.project;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.inject.Provider;

import org.kuali.common.jute.enc.openssl.OpenSSLModule;
import org.kuali.common.jute.env.EnvModule;
import org.kuali.common.jute.json.jackson.JacksonModule;
import org.kuali.common.jute.process.ProcessModule;
import org.kuali.common.jute.runtime.RuntimeModule;
import org.kuali.common.jute.system.SystemModule;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;

public final class BasicModuleProvider implements Provider<Iterable<AbstractModule>> {

    @Override
    public Iterable<AbstractModule> get() {
        List<AbstractModule> list = newArrayList();
        list.add(new SystemModule());
        list.add(new EnvModule());
        list.add(new RuntimeModule());
        list.add(new ProcessModule());
        list.add(new JacksonModule());
        list.add(new OpenSSLModule());
        return ImmutableList.copyOf(list);
    }

}
