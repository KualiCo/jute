package org.kuali.common.jute.scm;

import static com.google.common.base.CharMatcher.WHITESPACE;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Verify.verify;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.File;
import java.io.IOException;

import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.process.ProcessResult;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.scm.annotation.ScmDirectory;
import org.kuali.common.jute.scm.annotation.ScmRevisionTimeout;

import com.google.common.io.ByteSource;

public final class GitRevisionProvider implements Provider<String> {

    public GitRevisionProvider(@ScmDirectory File directory, ProcessService service, @ScmRevisionTimeout long timeoutMillis) {
        this.directory = checkNotNull(directory, "directory");
        this.service = checkNotNull(service, "service");
        this.timeoutMillis = checkMin(timeoutMillis, 0, "timeoutMillis");
    }

    private final File directory;
    private final ProcessService service;
    private final long timeoutMillis;

    @Override
    public String get() {
        ProcessContext.Builder builder = ProcessContext.builder();
        builder.withCommand("git");
        builder.withArgs(asList("rev-parse", "--verify", "HEAD"));
        builder.withDirectory(directory);
        builder.withTimeoutMillis(timeoutMillis);
        ProcessContext context = builder.build();
        try {
            ProcessResult result = service.execute(context);
            verify(result.getExitValue() == 0, "non-zero exit value -> %s", result.getExitValue());
            ByteSource stdin = result.getStdin();
            return WHITESPACE.trimFrom(new String(stdin.read(), UTF_8));
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
