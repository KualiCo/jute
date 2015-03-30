package org.kuali.common.jute.process;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Stopwatch.createStarted;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Maps.fromProperties;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.ByteStreams.toByteArray;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.kuali.common.jute.base.Precondition.checkMax;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.base.Threads.sleep;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.kuali.common.jute.base.TimedInterval;
import org.kuali.common.jute.system.VirtualSystem;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.base.VerifyException;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;

public final class DefaultProcessService implements ProcessService {

    @Inject
    public DefaultProcessService(VirtualSystem system) {
        this.system = checkNotNull(system, "system");
    }

    private final VirtualSystem system;

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

        // Add default environment variables (if requested)
        if (context.isInheritEnvironment()) {
            // copy system environment into a Map<String,String>
            pb.environment().putAll(fromProperties(system.getEnvironment()));
        }

        // Add custom environment variables (if any)
        pb.environment().putAll(context.getEnvironment());

        Stopwatch sw = createStarted();
        Process process = pb.start();
        while (isAlive(process)) {
            checkedSleep(context.getSleepMillis(), context.getTimeoutMillis(), sw);
        }

        // preserve the timing of this process execution
        TimedInterval timing = TimedInterval.build(sw);

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
            ProcessResult result = builder.build();

            // validate the exit value (if needed)
            checkExitValue(result, context);

            return result;
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
            long max = timeout.get();
            checkMax(elapsed, max, "elapsed");
        }
    }

    private void checkExitValue(ProcessResult result, ProcessContext context) throws IOException {

        List<Integer> allowedValues = context.getAllowedExitValues();

        // no exit values were supplied
        if (allowedValues.isEmpty()) {
            return;
        }

        // if the exit value is in that range, we are good to go
        if (allowedValues.contains(result.getExitValue())) {
            return;
        }

        // we've been supplied with a range of exit values, but the actual exit value was not in that range
        String cmd = context.getCommand();
        String args = Joiner.on(' ').join(context.getArgs());
        String msg = "invalid exit value '%s', allowed values %s -> [%s %s]\n\nstderr: [%s]\n\nstdout: [%s]";
        String stderr = result.getStderr().asCharSource(UTF_8).read();
        String stdout = result.getStdout().asCharSource(UTF_8).read();
        throw new VerifyException(format(msg, result.getExitValue(), allowedValues, cmd, args, stderr, stdout));
    }

    // check to see if the process is still alive
    private boolean isAlive(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    public VirtualSystem getSystem() {
        return system;
    }

}
