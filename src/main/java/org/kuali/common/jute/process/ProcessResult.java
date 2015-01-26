package org.kuali.common.jute.process;

import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import org.kuali.common.jute.base.TimedInterval;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.io.ByteSource;

@JsonDeserialize(builder = ProcessResult.Builder.class)
public final class ProcessResult {

    private final int exitValue;
    private final ByteSource stdout;
    private final ByteSource stderr;
    private final TimedInterval timing;

    private ProcessResult(Builder builder) {
        this.exitValue = builder.exitValue;
        this.stdout = builder.stdout;
        this.stderr = builder.stderr;
        this.timing = builder.timing;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProcessResult> {

        private int exitValue;
        private ByteSource stdout;
        private ByteSource stderr;
        private TimedInterval timing;

        public Builder withExitValue(int exitValue) {
            this.exitValue = exitValue;
            return this;
        }

        public Builder withStdout(ByteSource stdout) {
            this.stdout = stdout;
            return this;
        }

        public Builder withStderr(ByteSource stderr) {
            this.stderr = stderr;
            return this;
        }

        public Builder withTiming(TimedInterval timing) {
            this.timing = timing;
            return this;
        }

        @Override
        public ProcessResult build() {
            return checkNoNulls(new ProcessResult(this));
        }
    }

    public int getExitValue() {
        return exitValue;
    }

    public ByteSource getStdout() {
        return stdout;
    }

    public ByteSource getStderr() {
        return stderr;
    }

    public TimedInterval getTiming() {
        return timing;
    }

}