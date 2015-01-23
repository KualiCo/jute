package org.kuali.common.jute.process;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.List;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ProcessModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProcessService.class).to(DefaultProcessService.class);
    }

    @Provides
    protected ProcessContext processContext() {
        String command = "git";
        List<String> args = asList("rev-parse", "--verify", "HEAD");
        File directory = new File(".");
        ProcessContext.Builder builder = ProcessContext.builder();
        builder.withArgs(args);
        builder.withCommand(command);
        builder.withDirectory(Optional.of(directory));
        return builder.build();
    }

}
