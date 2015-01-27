package org.kuali.common.jute.process;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@JsonDeserialize(builder = ProcessContext.Builder.class)
public final class ProcessContext {

    private final String command;
    private final ImmutableList<String> args;
    private final Optional<File> directory;
    private final Optional<Long> timeoutMillis;
    private final long sleepMillis;
    private final ImmutableMap<String, String> environment;

    private ProcessContext(Builder builder) {
        this.command = builder.command;
        this.args = ImmutableList.copyOf(builder.args);
        this.directory = builder.directory;
        this.timeoutMillis = builder.timeoutMillis;
        this.sleepMillis = builder.sleepMillis;
        this.environment = ImmutableMap.copyOf(builder.environment);
    }

    public static ProcessContext build(String command) {
        return build(command, (String[]) null);
    }

    public static ProcessContext build(String command, String... args) {
        Builder builder = builder();
        builder.withCommand(command);
        if (args != null && args.length > 0) {
            builder.withArgs(ImmutableList.copyOf(args));
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProcessContext> {

        private String command;
        private List<String> args = newArrayList();
        private Optional<File> directory = absent();
        private Optional<Long> timeoutMillis = absent();
        private long sleepMillis = 10;
        private Map<String, String> environment = newHashMap();

        public Builder withEnvironment(Map<String, String> environment) {
            this.environment = environment;
            return this;
        }

        public Builder withCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder withArgs(List<String> args) {
            this.args = args;
            return this;
        }

        @JsonSetter
        public Builder withDirectory(Optional<File> directory) {
            this.directory = directory;
            return this;
        }

        public Builder withDirectory(File directory) {
            return withDirectory(fromNullable(directory));
        }

        @JsonSetter
        public Builder withTimeoutMillis(Optional<Long> timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        public Builder withTimeoutMillis(long timeoutMillis) {
            return withTimeoutMillis(Optional.of(timeoutMillis));
        }

        public Builder withSleepMillis(long sleepMillis) {
            this.sleepMillis = sleepMillis;
            return this;
        }

        @Override
        public ProcessContext build() {
            return validate(checkNoNulls(new ProcessContext(this)));
        }

        private static ProcessContext validate(ProcessContext instance) {
            checkNotBlank(instance.command, "command");
            checkMin(instance.timeoutMillis, 0, "timeoutMillis");
            checkMin(instance.sleepMillis, 1, "sleepMillis");
            return instance;
        }
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    public Optional<File> getDirectory() {
        return directory;
    }

    public Optional<Long> getTimeoutMillis() {
        return timeoutMillis;
    }

    public long getSleepMillis() {
        return sleepMillis;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

}
