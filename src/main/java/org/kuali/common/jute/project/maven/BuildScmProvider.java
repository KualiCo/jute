package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import javax.inject.Provider;

import org.kuali.common.jute.project.BuildScm;

import com.google.common.base.Optional;

public final class BuildScmProvider implements Provider<Optional<BuildScm>> {

    public BuildScmProvider(DirectoryContext dirs, Optional<Scm> scm, boolean skip) {
        this.dirs = checkNotNull(dirs, "dirs");
        this.scm = checkNotNull(scm, "scm");
        this.skip = skip;
    }

    private final DirectoryContext dirs;
    private final Optional<Scm> scm;
    private final boolean skip;

    @Override
    public Optional<BuildScm> get() {
        return absent();
    }

    public Optional<Scm> getScm() {
        return scm;
    }

    public boolean isSkip() {
        return skip;
    }

    public DirectoryContext getDirs() {
        return dirs;
    }
}
