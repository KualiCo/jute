package org.kuali.common.jute.scm;

import static com.google.common.base.Optional.absent;
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
import org.kuali.common.jute.scm.annotation.Directory;
import org.kuali.common.jute.scm.annotation.Skip;
import org.kuali.common.jute.scm.annotation.Timeout;

import com.google.common.base.Optional;
import com.google.common.io.ByteSource;

public final class SvnRevisionProvider implements Provider<Optional<String>> {

    private final File directory;
    private final ProcessService service;
    private final long timeoutMillis;
    private final boolean skip;

    @Override
    public Optional<String> get() {
        if (skip) {
            return absent();
        }
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
            return Optional.of(trimmed);
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

    private SvnRevisionProvider(Builder builder) {
        this.directory = builder.directory;
        this.service = builder.service;
        this.timeoutMillis = builder.timeoutMillis;
        this.skip = builder.skip;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements org.apache.commons.lang3.builder.Builder<SvnRevisionProvider>, Provider<SvnRevisionProvider> {

        private File directory;
        private ProcessService service;
        private long timeoutMillis = getMillis("30s");
        private boolean skip = false;

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

        @Inject
        public Builder withSkip(@Skip boolean skip) {
            this.skip = skip;
            return this;
        }

        @Override
        public SvnRevisionProvider get() {
            return build();
        }

        @Override
        public SvnRevisionProvider build() {
            return checkNoNulls(new SvnRevisionProvider(this));
        }
    }

}
