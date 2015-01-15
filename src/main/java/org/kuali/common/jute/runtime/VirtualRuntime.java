/**
 * Copyright 2014-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.common.jute.runtime;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;
import static org.kuali.common.jute.base.Optionals.fromNegativeToAbsent;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.runtime.annotation.GarbageCollectionEvents;
import org.kuali.common.jute.runtime.annotation.ProcessId;
import org.kuali.common.jute.runtime.annotation.Processors;
import org.kuali.common.jute.runtime.annotation.SystemLoadAverage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = VirtualRuntime.Builder.class)
public final class VirtualRuntime {

    private final int processors;
    private final Memory memory;
    private final Uptime uptime;
    private final Optional<Integer> pid;
    private final ClassLoading classLoading;
    private final Threads threads;
    private final ImmutableList<GarbageCollectionEvent> garbageCollectionEvents;
    private final Optional<Double> systemLoadAverage;

    private VirtualRuntime(Builder builder) {
        this.processors = builder.processors;
        this.memory = builder.memory;
        this.uptime = builder.uptime;
        this.pid = builder.pid;
        this.systemLoadAverage = builder.systemLoadAverage;
        this.classLoading = builder.classLoading;
        this.threads = builder.threads;
        this.garbageCollectionEvents = ImmutableList.copyOf(builder.garbageCollectionEvents);
    }

    public static VirtualRuntime build() {
        Builder builder = builder();
        builder.withProcessors(Runtime.getRuntime().availableProcessors());
        builder.withMemory(Memory.build());
        builder.withUptime(Uptime.build());
        builder.withPid(ProcessIdProvider.INSTANCE.get());
        builder.withClassLoading(ClassLoading.build());
        builder.withThreads(Threads.build());
        builder.withGarbageCollectionEvents(GarbageCollectionEvent.buildAll());
        builder.withSystemLoadAverage(fromNegativeToAbsent(getOperatingSystemMXBean().getSystemLoadAverage()));
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<VirtualRuntime>, Provider<VirtualRuntime> {

        private int processors;
        private Memory memory;
        private Uptime uptime;
        private Optional<Integer> pid = absent();
        private ClassLoading classLoading;
        private Threads threads;
        private List<GarbageCollectionEvent> garbageCollectionEvents = newArrayList();
        private Optional<Double> systemLoadAverage = absent();

        @Inject
        public Builder withSystemLoadAverage(@SystemLoadAverage Optional<Double> systemLoadAverage) {
            this.systemLoadAverage = systemLoadAverage;
            return this;
        }

        @Inject
        public Builder withGarbageCollectionEvents(@GarbageCollectionEvents List<GarbageCollectionEvent> garbageCollectionEvents) {
            this.garbageCollectionEvents = garbageCollectionEvents;
            return this;
        }

        @Inject
        public Builder withThreads(Threads threads) {
            this.threads = threads;
            return this;
        }

        @Inject
        public Builder withClassLoading(ClassLoading classLoading) {
            this.classLoading = classLoading;
            return this;
        }

        @Inject
        public Builder withPid(@ProcessId Optional<Integer> pid) {
            this.pid = pid;
            return this;
        }

        @Inject
        public Builder withUptime(Uptime uptime) {
            this.uptime = uptime;
            return this;
        }

        @Inject
        public Builder withProcessors(@Processors int processors) {
            this.processors = processors;
            return this;
        }

        @Inject
        public Builder withMemory(Memory memory) {
            this.memory = memory;
            return this;
        }

        @Override
        public VirtualRuntime get() {
            return build();
        }

        @Override
        public VirtualRuntime build() {
            return checkNoNulls(new VirtualRuntime(this));
        }
    }

    public int getProcessors() {
        return processors;
    }

    public Memory getMemory() {
        return memory;
    }

    public Uptime getUptime() {
        return uptime;
    }

    public Optional<Integer> getPid() {
        return pid;
    }

    public ClassLoading getClassLoading() {
        return classLoading;
    }

    public Threads getThreads() {
        return threads;
    }

    public List<GarbageCollectionEvent> getGarbageCollectionEvents() {
        return garbageCollectionEvents;
    }

    public Optional<Double> getSystemLoadAverage() {
        return systemLoadAverage;
    }

}
