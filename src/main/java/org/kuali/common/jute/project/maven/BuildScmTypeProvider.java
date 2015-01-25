package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.project.maven.annotation.SkipProjectScm;
import org.kuali.common.jute.scm.ScmType;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;

public final class BuildScmTypeProvider implements Provider<Optional<ScmType>> {

    @Inject
    public BuildScmTypeProvider(Project project, @SkipProjectScm boolean skip) {
        this.project = checkNotNull(project, "project");
        this.skip = skip;
    }

    private final Project project;
    private final boolean skip;

    @Override
    public Optional<ScmType> get() {
        if (skip) {
            return absent();
        }
        Optional<MavenScm> scm = project.getScm();
        if (!scm.isPresent()) {
            return absent();
        } else {
            return getScmType(scm.get());
        }
    }

    private Optional<ScmType> getScmType(MavenScm scm) {
        Optional<ScmType> dev = getScmType(scm.getDeveloperConnection());
        if (dev.isPresent()) {
            return dev;
        }
        Optional<ScmType> conn = getScmType(scm.getConnection());
        if (conn.isPresent()) {
            return conn;
        } else {
            return absent();
        }
    }

    private Optional<ScmType> getScmType(Optional<String> url) {
        if (!url.isPresent()) {
            return absent();
        }
        List<String> tokens = Splitter.on(':').omitEmptyStrings().trimResults().splitToList(url.get());
        checkMin(tokens.size(), 2, "tokens.size()");
        String type = tokens.get(1);
        try {
            return Optional.of(ScmType.valueOf(type.toUpperCase()));
        } catch (Exception e) {
            return absent();
        }
    }

    public Project getProject() {
        return project;
    }

    public boolean isSkip() {
        return skip;
    }

}
