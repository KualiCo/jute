package org.kuali.common.jute.base;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.kuali.common.jute.base.Precondition.checkEquals;
import static org.kuali.common.jute.base.Precondition.checkMin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Stopwatch;

/**
 * Holds information related to a period of time.
 *
 * Stop is guaranteed to be greater than or equal to start.
 *
 * Elapsed is guaranteed to be the difference between stop and start (might be zero)
 */
@JsonDeserialize(builder = TimedInterval.Builder.class)
public final class TimedInterval {

    private final long start;
    private final long stop;
    private final long elapsed;

    private TimedInterval(Builder builder) {
        this.start = builder.start;
        this.stop = builder.stop;
        this.elapsed = builder.elapsed;
    }

    public static TimedInterval build(RunningStopwatch sw) {
        return build(sw.elapsedMillis());
    }

    public static TimedInterval build(Stopwatch sw) {
        return build(sw.elapsed(MILLISECONDS));
    }

    public static TimedInterval build(long elapsed) {
        // get the current timestamp from the system clock and use it to calculate the start time
        long stop = currentTimeMillis();
        long start = stop - elapsed;
        return builder().withStart(start).withStop(stop).withElapsed(elapsed).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<TimedInterval> {

        private long start;
        private long stop;
        private long elapsed;

        public Builder withStart(long start) {
            this.start = start;
            return this;
        }

        public Builder withStop(long stop) {
            this.stop = stop;
            return this;
        }

        public Builder withElapsed(long elapsed) {
            this.elapsed = elapsed;
            return this;
        }

        @Override
        public TimedInterval build() {
            return validate(new TimedInterval(this));
        }

        private static TimedInterval validate(TimedInterval instance) {
            checkMin(instance.stop, instance.start, "stop"); // stop can't be less than start
            checkEquals(instance.elapsed, instance.stop - instance.start, "elapsed"); // elapsed must exactly equal stop - start
            return instance;
        }

    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public long getElapsed() {
        return elapsed;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("start", start).add("stop", stop).add("elapsed", elapsed).toString();
    }

}
