package org.kuali.common.jute.process;

import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ProcessRunnable.Builder.class)
public final class ProcessRunnable implements Runnable {

    private final ProcessService service;
    private final ProcessContext context;

    @Override
    public void run() {
        try {
            service.execute(context);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private ProcessRunnable(Builder builder) {
        this.service = builder.service;
        this.context = builder.context;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<ProcessRunnable>, Provider<ProcessRunnable> {

        private ProcessService service;
        private ProcessContext context;

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

}
