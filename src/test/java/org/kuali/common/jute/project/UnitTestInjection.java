package org.kuali.common.jute.project;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;

import java.util.List;

import org.kuali.common.jute.project.maven.KualiJuteModule;
import org.kuali.common.jute.project.maven.ProjectModule;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public final class UnitTestInjection {

    private UnitTestInjection() {}

    public static List<AbstractModule> getUnitTestModules() {
        List<AbstractModule> list = newArrayList();
        list.addAll(newArrayList(new BasicModuleProvider().get()));
        list.add(new KualiJuteModule());
        list.add(new ProjectModule());
        return ImmutableList.copyOf(list);
    }

    public static Injector createUnitTestInjector() {
        return createInjector(concat(new BasicModuleProvider().get(), asList(new KualiJuteModule(), new ProjectModule())));
    }
}
