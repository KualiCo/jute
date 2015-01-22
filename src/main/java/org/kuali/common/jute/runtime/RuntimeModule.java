package org.kuali.common.jute.runtime;

import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;
import static org.kuali.common.jute.base.Optionals.fromNegativeToAbsent;

import java.util.List;

import javax.inject.Provider;

import org.kuali.common.jute.runtime.annotation.GarbageCollectionEvents;
import org.kuali.common.jute.runtime.annotation.ProcessId;
import org.kuali.common.jute.runtime.annotation.Processors;
import org.kuali.common.jute.runtime.annotation.SystemLoadAverage;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class RuntimeModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Processors.class).to(Runtime.getRuntime().availableProcessors());
        bind(Memory.class).toProvider(MemoryProvider.INSTANCE);
        bind(Uptime.class).toProvider(UptimeProvider.INSTANCE);
        bind(new TypeLiteral<Optional<Integer>>() {}).annotatedWith(ProcessId.class).toProvider(ProcessIdProvider.INSTANCE).asEagerSingleton();
        bind(ClassLoading.class).toProvider(ClassLoadingProvider.INSTANCE);
        bind(Threads.class).toProvider(ThreadsProvider.INSTANCE);
        bind(garbageCollectionEventList()).annotatedWith(GarbageCollectionEvents.class).toProvider(GarbageCollectionEventsProvider.INSTANCE);
        bind(new TypeLiteral<Optional<Double>>() {}).annotatedWith(SystemLoadAverage.class).toProvider(SystemLoadAverageProvider.INSTANCE);
        bind(VirtualRuntime.class).toProvider(VirtualRuntime.Builder.class);
    }

    private enum MemoryProvider implements Provider<Memory> {
        INSTANCE;
        @Override
        public Memory get() {
            return Memory.build();
        }
    }

    private enum SystemLoadAverageProvider implements Provider<Optional<Double>> {
        INSTANCE;
        @Override
        public Optional<Double> get() {
            return fromNegativeToAbsent(getOperatingSystemMXBean().getSystemLoadAverage());
        }
    }

    private enum UptimeProvider implements Provider<Uptime> {
        INSTANCE;
        @Override
        public Uptime get() {
            return Uptime.build();
        }
    }

    private enum ClassLoadingProvider implements Provider<ClassLoading> {
        INSTANCE;
        @Override
        public ClassLoading get() {
            return ClassLoading.build();
        }
    }

    private enum GarbageCollectionEventsProvider implements Provider<List<GarbageCollectionEvent>> {
        INSTANCE;
        @Override
        public List<GarbageCollectionEvent> get() {
            return GarbageCollectionEvent.buildAll();
        }
    }

    private enum ThreadsProvider implements Provider<Threads> {
        INSTANCE;
        @Override
        public Threads get() {
            return Threads.build();
        }
    }

    private static TypeLiteral<List<GarbageCollectionEvent>> garbageCollectionEventList() {
        return new TypeLiteral<List<GarbageCollectionEvent>>() {};
    }

}
