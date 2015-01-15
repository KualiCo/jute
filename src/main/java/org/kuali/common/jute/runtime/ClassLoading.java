package org.kuali.common.jute.runtime;

import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ClassLoading.Builder.class)
public final class ClassLoading {

    private final int current;
    private final long total;
    private final long unloaded;

    private ClassLoading(Builder builder) {
        this.current = builder.current;
        this.total = builder.total;
        this.unloaded = builder.unloaded;
    }

    public static ClassLoading build() {
        ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean();
        Builder builder = builder();
        builder.withCurrent(bean.getLoadedClassCount());
        builder.withTotal(bean.getTotalLoadedClassCount());
        builder.withUnloaded(bean.getUnloadedClassCount());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ClassLoading> {

        private int current;
        private long total;
        private long unloaded;

        public Builder withCurrent(int current) {
            this.current = current;
            return this;
        }

        public Builder withTotal(long total) {
            this.total = total;
            return this;
        }

        public Builder withUnloaded(long unloaded) {
            this.unloaded = unloaded;
            return this;
        }

        @Override
        public ClassLoading build() {
            return validate(new ClassLoading(this));
        }

        private static ClassLoading validate(ClassLoading instance) {
            checkNoNulls(instance);
            checkMin(instance.current, 0, "current");
            checkMin(instance.total, 0, "total");
            checkMin(instance.unloaded, 0, "unloaded");
            return instance;
        }
    }

    public int getCurrent() {
        return current;
    }

    public long getTotal() {
        return total;
    }

    public long getUnloaded() {
        return unloaded;
    }

}
