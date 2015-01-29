package org.kuali.common.jute.process;

import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.IOException;

public final class ProcessRunnable implements Runnable {

    public ProcessRunnable(ProcessService service, ProcessContext context) {
        this.service = checkNotNull(service, "service");
        this.context = checkNotNull(context, "context");
    }

    private final ProcessService service;
    private final ProcessContext context;

    public ProcessService getService() {
        return service;
    }

    public ProcessContext getContext() {
        return context;
    }

    @Override
    public void run() {
        try {
            service.execute(context);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

}
