package org.kuali.common.jute.project;

import static com.google.common.collect.Iterables.concat;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;

import org.kuali.common.jute.project.maven.KualiJuteModule;

import com.google.inject.Injector;

public final class UnitTestInjection {

    private UnitTestInjection() {}

    public static Injector createUnitTestInjector() {
        return createInjector(concat(new BasicModuleProvider().get(), asList(new KualiJuteModule())));
    }

}
