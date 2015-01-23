package org.kuali.common.jute.process;

import static com.google.common.base.Stopwatch.createStarted;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.io.ByteSource.wrap;
import static com.google.common.io.ByteStreams.toByteArray;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.kuali.common.jute.base.Exceptions.ioException;
import static org.kuali.common.jute.base.Formats.getTime;
import static org.kuali.common.jute.base.Threads.sleep;

import java.io.IOException;
import java.util.List;

import org.kuali.common.jute.base.TimedInterval;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSource;

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
            pb.directory(context.getDirectory().get());
        }
        Stopwatch sw = createStarted();
        Process process = pb.start();
        while (isAlive(process)) {
            sleep(context.getSleepMillis());
            if (context.getTimeoutMillis().isPresent()) {
                long elapsed = sw.elapsed(MILLISECONDS);
                long timeout = context.getTimeoutMillis().get();
                if (elapsed > timeout) {
                    throw ioException("timeout exceeded -> [max: %s, elapsed: %s]", getTime(timeout), getTime(elapsed));
                }
            }
        }

        TimedInterval timing = TimedInterval.build(sw);
        int exitValue = process.exitValue();
        ByteSource stdin = wrap(toByteArray(process.getInputStream()));
        ByteSource stderr = wrap(toByteArray(process.getErrorStream()));

        ProcessResult.Builder builder = ProcessResult.builder();
        builder.withExitValue(exitValue);
        builder.withStderr(stderr);
        builder.withStdin(stdin);
        builder.withTiming(timing);
        return builder.build();
    }

    protected boolean isAlive(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

}
