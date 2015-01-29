package org.kuali.common.jute.project.maven.version;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.project.maven.DirectoryContext;
import org.kuali.common.jute.project.maven.annotation.Version;

import com.google.common.collect.ImmutableList;

public final class SetVersionProcessContextProvider implements Provider<ProcessContext> {

    @Inject
    public SetVersionProcessContextProvider(DirectoryContext dirs, @Version String version) {
        this.dirs = checkNotNull(dirs, "dirs");
        this.version = checkNotBlank(version, "version");
    }

    private final DirectoryContext dirs;
    private final String version;

    @Override
    public ProcessContext get() {

        // setup the info needed for the process context
        String command = "mvn";
        List<String> args = getArgs(version);
        File directory = dirs.getBasedir();

        // create an immutable process context
        ProcessContext.Builder builder = ProcessContext.builder();
        builder.withCommand(command);
        builder.withArgs(args);
        builder.withDirectory(directory);
        return builder.build();
    }

    private List<String> getArgs(String version) {
        List<String> args = newArrayList();
        args.add("versions:set");
        args.add(format("-DnewVersion=%s", version));
        args.add("-N");
        return ImmutableList.copyOf(args);
    }

}
