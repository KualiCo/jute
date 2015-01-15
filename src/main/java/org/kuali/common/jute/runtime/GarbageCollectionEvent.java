package org.kuali.common.jute.runtime;

import static com.google.common.collect.Lists.newArrayList;
import static org.kuali.common.jute.base.Optionals.fromNegativeToAbsent;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@JsonDeserialize(builder = GarbageCollectionEvent.Builder.class)
public final class GarbageCollectionEvent {

    private final String name;
    private final Optional<Long> count;
    private final Optional<Long> millis;

    private GarbageCollectionEvent(Builder builder) {
        this.name = builder.name;
        this.count = builder.count;
        this.millis = builder.millis;
    }

    public static List<GarbageCollectionEvent> buildAll() {
        List<GarbageCollectionEvent> events = newArrayList();
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean bean : beans) {
            GarbageCollectionEvent event = copyOf(bean);
            events.add(event);
        }
        return ImmutableList.copyOf(events);
    }

    public static GarbageCollectionEvent copyOf(GarbageCollectorMXBean bean) {
        Builder builder = builder();
        builder.withName(bean.getName());
        builder.withCount(fromNegativeToAbsent(bean.getCollectionCount()));
        builder.withMillis(fromNegativeToAbsent(bean.getCollectionTime()));
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<GarbageCollectionEvent> {

        private String name;
        private Optional<Long> count;
        private Optional<Long> millis;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCount(Optional<Long> count) {
            this.count = count;
            return this;
        }

        public Builder withMillis(Optional<Long> millis) {
            this.millis = millis;
            return this;
        }

        @Override
        public GarbageCollectionEvent build() {
            return validate(new GarbageCollectionEvent(this));
        }

        public static GarbageCollectionEvent validate(GarbageCollectionEvent instance) {
            checkNoNulls(instance);
            checkNotBlank(instance.name, "name");
            checkMin(instance.count, 0, "count");
            checkMin(instance.millis, 0, "millis");
            return instance;
        }
    }

    public String getName() {
        return name;
    }

    public Optional<Long> getCount() {
        return count;
    }

    public Optional<Long> getMillis() {
        return millis;
    }

}
