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
import static org.kuali.common.jute.scm.ScmType.SVN;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.process.ProcessResult;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.project.BuildScm;

import com.google.common.io.ByteSource;

public final class SvnScmProvider implements Provider<BuildScm> {

    public SvnScmProvider(File directory, ProcessService service, long timeoutMillis) {
        this.directory = checkNotNull(directory, "directory");
        this.service = checkNotNull(service, "service");
        this.timeoutMillis = checkMin(timeoutMillis, 0, "timeoutMillis");
    }

    private final File directory;
    private final ProcessService service;
    private final long timeoutMillis;

    @Override
    public BuildScm get() {
        ProcessContext.Builder builder = ProcessContext.builder();
        builder.withCommand("svn");
        builder.withArgs(asList("info"));
        builder.withDirectory(directory);
        builder.withTimeoutMillis(timeoutMillis);
        ProcessContext context = builder.build();
        try {
            ProcessResult result = service.execute(context);
            verify(result.getExitValue() == 0, "non-zero exit value -> %s", result.getExitValue());
            ByteSource stdout = result.getStdout();
            List<String> lines = readLines(new InputStreamReader(new ByteArrayInputStream(stdout.read())));
            String revision = getRevision(lines);
            String url = getLine(lines, "URL:");
            return BuildScm.builder().withRevision(revision).withUrl(url).withType(SVN).build();
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    private String getRevision(List<String> lines) {
        String revision = getLine(lines, "Revision:");
        parseLong(revision);
        return revision;
    }

    private String getLine(List<String> lines, String prefix) {
        Iterable<String> filtered = filter(lines, containsPattern("^" + prefix));
        String revision = getSingleElement(filtered);
        return trimToNull(removeStart(revision, prefix));
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
