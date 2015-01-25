package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.project.maven.annotation.SkipProjectScm;

import com.google.common.base.Optional;

public final class BuildScmProvider implements Provider<Optional<BuildScm>> {

    @Inject
    public BuildScmProvider(DirectoryContext dirs, Project project, @SkipProjectScm boolean skip) {
        this.dirs = checkNotNull(dirs, "dirs");
        this.project = checkNotNull(project, "project");
        this.skip = skip;
    }

    private final DirectoryContext dirs;
    private final Project project;
    private final boolean skip;

    @Override
    public Optional<BuildScm> get() {
        return absent();
    }

    public Project getProject() {
        return project;
    }

    public boolean isSkip() {
        return skip;
    }

    public DirectoryContext getDirs() {
        return dirs;
    }
}
