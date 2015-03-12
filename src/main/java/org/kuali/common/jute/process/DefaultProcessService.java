package org.kuali.common.jute.process;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Stopwatch.createStarted;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.ByteStreams.toByteArray;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.kuali.common.jute.base.Exceptions.ioException;
import static org.kuali.common.jute.base.Formats.getTime;
import static org.kuali.common.jute.base.Threads.sleep;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.kuali.common.jute.base.TimedInterval;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.base.VerifyException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;

public class DefaultProcessService implements ProcessService {

    @Override
    public ProcessResult execute(String command) throws IOException {
        return execute(ProcessContext.build(command));
    }

    @Override
    public ProcessResult execute(ProcessContext context) throws IOException {
        List<String> command = ImmutableList.copyOf(concat(asList(context.getCommand()), context.getArgs()));
        ProcessBuilder pb = new ProcessBuilder(command);
        if (context.getDirectory().isPresent()) {
            File directory = context.getDirectory().get();
            checkArgument(directory.isDirectory(), "[%s] must be an existing directory", directory);
            pb.directory(directory);
        }

        // Add environment variables
        pb.environment().putAll(context.getEnvironment());

        Stopwatch sw = createStarted();
        Process process = pb.start();
        while (isAlive(process)) {
            checkedSleep(context.getSleepMillis(), context.getTimeoutMillis(), sw);
        }

        // preserve the timing of this process execution
        TimedInterval timing = TimedInterval.build(sw);

        // we may need to validate the exit value
        checkExitValue(process.exitValue(), context);

        // capture the output of the process into a result object
        Closer closer = Closer.create();
        try {
            ProcessResult.Builder builder = ProcessResult.builder();
            ByteSource stdout = wrap(toByteArray(closer.register(process.getInputStream())));
            ByteSource stderr = wrap(toByteArray(closer.register(process.getErrorStream())));
            builder.withExitValue(process.exitValue());
            builder.withStderr(stderr);
            builder.withStdout(stdout);
            builder.withTiming(timing);
            return builder.build();
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    private void checkedSleep(long sleep, Optional<Long> timeout, Stopwatch sw) throws IOException {
        sleep(sleep);
        if (timeout.isPresent()) {
            long elapsed = sw.elapsed(MILLISECONDS);
            long millis = timeout.get();
            if (elapsed > millis) {
                throw ioException("timeout exceeded -> [max: %s, elapsed: %s]", getTime(millis), getTime(elapsed));
            }
        }
    }

    private void checkExitValue(int exitValue, ProcessContext context) {

        // no exit values were supplied
        if (!context.getAllowedExitValues().isPresent()) {
            return;
        }

        // extract the range of allowed exit values
        Range<Integer> range = context.getAllowedExitValues().get();

        // if the exit value is in that range, we are good to go
        if (range.contains(exitValue)) {
            return;
        }

        // we've been supplied with a range of exit values, but the actual exit value was not in that range
        String cmd = context.getCommand();
        String args = Joiner.on(' ').join(context.getArgs());
        String msg = "invalid exit value '%s', allowed value(s) '%s' -> [%s %s]";
        throw new VerifyException(format(msg, exitValue, range, cmd, args));
    }

    // check to see if the process is still alive
    protected boolean isAlive(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

}
