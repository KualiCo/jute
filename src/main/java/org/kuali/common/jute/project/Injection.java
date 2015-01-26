package org.kuali.common.jute.project;

import static com.google.inject.Guice.createInjector;

import com.google.inject.Injector;

public final class Injection {

    private Injection() {}

    public static Injector getBasicInjector() {
        return createInjector(new BasicModuleProvider().get());
    }

}
