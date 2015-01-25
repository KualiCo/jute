package org.kuali.common.jute.process;

import com.google.inject.AbstractModule;

public class ProcessModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProcessService.class).to(DefaultProcessService.class);
    }

}
