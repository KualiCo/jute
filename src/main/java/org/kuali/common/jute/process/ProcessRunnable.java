package org.kuali.common.jute.process;

import static com.google.common.collect.Range.closed;
import static java.lang.String.format;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Joiner;
import com.google.common.base.VerifyException;
import com.google.common.collect.Range;

@JsonDeserialize(builder = ProcessRunnable.Builder.class)
public final class ProcessRunnable implements Runnable {

    private final ProcessService service;
    private final ProcessContext context;
    private final Range<Integer> allowedExitValues;
    private final boolean validateExitValue;

    @Override
    public void run() {
        try {
            ProcessResult result = service.execute(context);
            int exitValue = result.getExitValue();
            if (validateExitValue && !allowedExitValues.contains(exitValue)) {
                String range = allowedExitValues.toString();
                String command = context.getCommand();
                String args = Joiner.on(' ').join(context.getArgs());
                String msg = "invalid exit value '%s', allowed values '%s' -> [%s %s]";
                throw new VerifyException(format(msg, exitValue, range, command, args));
            }
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private ProcessRunnable(Builder builder) {
        this.service = builder.service;
        this.context = builder.context;
        this.allowedExitValues = builder.allowedExitValues;
        this.validateExitValue = builder.validateExitValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProcessRunnable>, Provider<ProcessRunnable> {

        private ProcessService service;
        private ProcessContext context;
        private Range<Integer> allowedExitValues = closed(0, 0);
        private boolean validateExitValue = true;

        @Inject
        public Builder withService(ProcessService service) {
            this.service = service;
            return this;
        }

        @Inject
        public Builder withContext(ProcessContext context) {
            this.context = context;
            return this;
        }

        public Builder withAllowedExitValues(Range<Integer> allowedExitValues) {
            this.allowedExitValues = allowedExitValues;
            return this;
        }

        public Builder withValidateExitValue(boolean validateExitValue) {
            this.validateExitValue = validateExitValue;
            return this;
        }

        @Override
        public ProcessRunnable get() {
            return build();
        }

        @Override
        public ProcessRunnable build() {
            return checkNoNulls(new ProcessRunnable(this));
        }
    }

    public ProcessService getService() {
        return service;
    }

    public ProcessContext getContext() {
        return context;
    }

    public Range<Integer> getAllowedExitValues() {
        return allowedExitValues;
    }

    public boolean isValidateExitValue() {
        return validateExitValue;
    }

}
