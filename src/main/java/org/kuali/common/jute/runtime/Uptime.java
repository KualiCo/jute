package org.kuali.common.jute.runtime;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.base.Precondition.checkEquals;
import static org.kuali.common.jute.base.Precondition.checkMin;

import java.lang.management.ManagementFactory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Holds information related to JVM uptime
 */
@JsonDeserialize(builder = Uptime.Builder.class)
public final class Uptime {

    private final long start;
    private final long now;
    private final long elapsed;

    private Uptime(Builder builder) {
        this.start = builder.start;
        this.now = builder.now;
        this.elapsed = builder.elapsed;
    }

    public static Uptime build() {
        // ManagementFactory.getRuntimeMXBean().getUptime() doesn't appear to be very reliable (JDK 8u25 on Mac OS X)
        // Calculating a value for 'start' by subtracting the value returned by getUptime() from System.currentTimeMillis()
        // results in 'start' drifting upwards a few millis every few seconds, which is a lot of drift.
        // ManagementFactory.getRuntimeMXBean().getStartTime() returns a constant value, so we just go with that as the basis
        // for determining up time. The downside to this approach is that clock adjustments will mess with 'elapsed'.
        long now = currentTimeMillis();
        long start = ManagementFactory.getRuntimeMXBean().getStartTime();
        long elapsed = now - start;
        //
        // long now = currentTimeMillis();
        // long elapsed = ManagementFactory.getRuntimeMXBean().getUptime();
        // long start = now - elapsed;
        //
        return builder().withStart(start).withNow(now).withElapsed(elapsed).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<Uptime> {

        private long start;
        private long now;
        private long elapsed;

        public Builder withStart(long start) {
            this.start = start;
            return this;
        }

        public Builder withNow(long now) {
            this.now = now;
            return this;
        }

        public Builder withElapsed(long elapsed) {
            this.elapsed = elapsed;
            return this;
        }

        @Override
        public Uptime build() {
            return validate(new Uptime(this));
        }

        private static Uptime validate(Uptime instance) {
            checkMin(instance.now, instance.start, "now"); // now can't be less than start
            checkEquals(instance.elapsed, instance.now - instance.start, "elapsed"); // elapsed must exactly equal now - start
            return instance;
        }

    }

    public long getStart() {
        return start;
    }

    public long getNow() {
        return now;
    }

    public long getElapsed() {
        return elapsed;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("start", start).add("now", now).add("elapsed", elapsed).toString();
    }

}
