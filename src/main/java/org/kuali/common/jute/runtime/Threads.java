package org.kuali.common.jute.runtime;

import static org.kuali.common.jute.base.Precondition.checkMin;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Threads.Builder.class)
public final class Threads {

    private final int current;
    private final int daemon;
    private final int peak;

    private Threads(Builder builder) {
        this.current = builder.current;
        this.daemon = builder.daemon;
        this.peak = builder.peak;
    }

    public static Threads build() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        Builder builder = builder();
        builder.withCurrent(bean.getThreadCount());
        builder.withDaemon(bean.getDaemonThreadCount());
        builder.withPeak(bean.getPeakThreadCount());
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Threads> {

        private int current;
        private int daemon;
        private int peak;

        public Builder withCurrent(int current) {
            this.current = current;
            return this;
        }

        public Builder withDaemon(int daemon) {
            this.daemon = daemon;
            return this;
        }

        public Builder withPeak(int peak) {
            this.peak = peak;
            return this;
        }

        @Override
        public Threads build() {
            return validate(new Threads(this));
        }

        private static Threads validate(Threads instance) {
            checkMin(instance.current, 0, "current");
            checkMin(instance.daemon, 0, "daemon");
            checkMin(instance.peak, 0, "peak");
            return instance;
        }
    }

    public int getCurrent() {
        return current;
    }

    public int getDaemon() {
        return daemon;
    }

    public int getPeak() {
        return peak;
    }

}
