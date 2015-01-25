package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.process.ProcessService;
import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.scm.GitScmProvider;
import org.kuali.common.jute.scm.ScmType;
import org.kuali.common.jute.scm.SvnScmProvider;
import org.kuali.common.jute.scm.annotation.Timeout;

import com.google.common.base.Optional;

public final class BuildScmProvider implements Provider<Optional<BuildScm>> {

    @Inject
    public BuildScmProvider(DirectoryContext dirs, Optional<ScmType> type, ProcessService service, @Timeout long timeoutMillis) {
        this.dirs = checkNotNull(dirs, "dirs");
        this.service = checkNotNull(service, "service");
        this.type = checkNotNull(type, "type");
        this.timeoutMillis = checkMin(timeoutMillis, 0, "timeoutMillis");
    }

    private final DirectoryContext dirs;
    private final Optional<ScmType> type;
    private final ProcessService service;
    private final long timeoutMillis;

    @Override
    public Optional<BuildScm> get() {
        if (type.isPresent()) {
            return absent();
        }
        switch (type.get()) {
            case GIT:
                return Optional.of(new GitScmProvider(dirs.getBasedir(), service, timeoutMillis).get());
            case SVN:
                SvnScmProvider.Builder builder = SvnScmProvider.builder();
                builder.withDirectory(dirs.getBasedir());
                builder.withService(service);
                builder.withTimeoutMillis(timeoutMillis);
                SvnScmProvider provider = builder.build();
                return Optional.of(provider.get());
            default:
                return absent();
        }
    }

    public DirectoryContext getDirs() {
        return dirs;
    }

    public ProcessService getService() {
        return service;
    }
}
