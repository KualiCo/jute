package org.kuali.common.jute.scm;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Verify.verify;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.io.CharStreams.readLines;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.collect.Iterables.getSingleElement;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.process.ProcessResult;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.scm.annotation.ScmDirectory;
import org.kuali.common.jute.scm.annotation.ScmRevisionTimeout;

import com.google.common.io.ByteSource;

public final class SvnRevisionProvider implements Provider<String> {

    public SvnRevisionProvider(@ScmDirectory File directory, ProcessService service, @ScmRevisionTimeout long timeoutMillis) {
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
        builder.withCommand("svn");
        builder.withArgs(asList("info"));
        builder.withDirectory(directory);
        builder.withTimeoutMillis(timeoutMillis);
        ProcessContext context = builder.build();
        try {
            ProcessResult result = service.execute(context);
            verify(result.getExitValue() == 0, "non-zero exit value -> %s", result.getExitValue());
            ByteSource stdin = result.getStdin();
            List<String> lines = readLines(new InputStreamReader(new ByteArrayInputStream(stdin.read())));
            String prefix = "Revision:";
            Iterable<String> filtered = filter(lines, containsPattern("^" + prefix));
            String revision = getSingleElement(filtered);
            String trimmed = trimToNull(removeStart(revision, prefix));
            parseLong(trimmed); // make sure it's actually a number
            return trimmed;
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
