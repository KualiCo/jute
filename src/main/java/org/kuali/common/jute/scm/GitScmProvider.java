package org.kuali.common.jute.scm;

import static com.google.common.base.CharMatcher.WHITESPACE;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Verify.verify;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.scm.ScmType.GIT;

import java.io.File;
import java.io.IOException;

import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.process.ProcessResult;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.project.BuildScm;

import com.google.common.io.ByteSource;

public final class GitScmProvider implements Provider<BuildScm> {

    private static final String EXECUTABLE = "git";

    public GitScmProvider(File directory, ProcessService service, long timeoutMillis) {
        this.directory = checkNotNull(directory, "directory");
        this.service = checkNotNull(service, "service");
        this.timeoutMillis = checkMin(timeoutMillis, 0, "timeoutMillis");
    }

    private final File directory;
    private final ProcessService service;
    private final long timeoutMillis;

    @Override
    public BuildScm get() {
        String rev = getOneLineResponse("rev-parse", "--verify", "HEAD");
        String url = getOneLineResponse("config", "--get", "remote.origin.url");
        return BuildScm.builder().withRevision(rev).withUrl(url).withType(GIT).build();
    }

    private String getOneLineResponse(String... args) {
        ProcessContext.Builder builder = ProcessContext.builder();
        builder.withCommand(EXECUTABLE);
        if (args != null) {
            builder.withArgs(asList(args));
        }
        builder.withDirectory(directory);
        builder.withTimeoutMillis(timeoutMillis);
        ProcessContext context = builder.build();
        try {
            ProcessResult result = service.execute(context);
            verify(result.getExitValue() == 0, "non-zero exit value -> %s", result.getExitValue());
            ByteSource stdout = result.getStdout();
            return WHITESPACE.trimFrom(new String(stdout.read(), UTF_8));
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    public File getDirectory() {
        return directory;
    }

    public ProcessService getService() {
        return service;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

}
