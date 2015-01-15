package org.kuali.common.jute.base;

import static com.google.common.base.Stopwatch.createStarted;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

/**
 * Threadsafe stopwatch that begins running as soon as it is constructed and never stops
 */
public final class RunningStopwatch {

    public RunningStopwatch() {
        this.stopwatch = createStarted();
    }

    private final Stopwatch stopwatch;

    public long elapsedMillis() {
        return elapsed(MILLISECONDS);
    }

    public long elapsed(TimeUnit unit) {
        synchronized (stopwatch) {
            return stopwatch.elapsed(unit);
        }
    }

}
