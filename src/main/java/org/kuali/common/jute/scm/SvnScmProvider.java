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
import static org.kuali.common.jute.base.Formats.getMillis;
import static org.kuali.common.jute.collect.Iterables.getSingleElement;
import static org.kuali.common.jute.reflect.Reflection.checkNoNulls;
import static org.kuali.common.jute.scm.ScmType.SVN;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessContext;
import org.kuali.common.jute.process.ProcessResult;
import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.scm.annotation.Directory;
import org.kuali.common.jute.scm.annotation.Timeout;

import com.google.common.io.ByteSource;

public final class SvnScmProvider implements Provider<BuildScm> {

    private final File directory;
    private final ProcessService service;
    private final long timeoutMillis;

    @Override
    public BuildScm get() {
        ProcessContext.Builder builder = ProcessContext.builder();
        builder.withCommand("/usr/local/bin/svn");
        builder.withArgs(asList("info"));
        builder.withDirectory(directory);
        builder.withTimeoutMillis(timeoutMillis);
        ProcessContext context = builder.build();
        try {
            ProcessResult result = service.execute(context);
            verify(result.getExitValue() == 0, "non-zero exit value -> %s", result.getExitValue());
            ByteSource stdin = result.getStdin();
            List<String> lines = readLines(new InputStreamReader(new ByteArrayInputStream(stdin.read())));
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

    private SvnScmProvider(Builder builder) {
        this.directory = builder.directory;
        this.service = builder.service;
        this.timeoutMillis = builder.timeoutMillis;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<SvnScmProvider>, Provider<SvnScmProvider> {

        private File directory;
        private ProcessService service;
        private long timeoutMillis = getMillis("30s");

        @Inject
        public Builder withDirectory(@Directory File directory) {
            this.directory = directory;
            return this;
        }

        @Inject
        public Builder withService(ProcessService service) {
            this.service = service;
            return this;
        }

        @Inject
        public Builder withTimeoutMillis(@Timeout long timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        @Override
        public SvnScmProvider get() {
            return build();
        }

        @Override
        public SvnScmProvider build() {
            return checkNoNulls(new SvnScmProvider(this));
        }
    }

}
