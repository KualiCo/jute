package org.kuali.common.jute.scm;

import static com.google.common.base.Optional.absent;

import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.project.maven.MavenScm;
import org.kuali.common.jute.project.maven.Project;
import org.kuali.common.jute.project.maven.annotation.SkipProjectScm;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ScmModule extends AbstractModule {

    @Override
    public void configure() {}

    @Provides
    protected Optional<BuildScm> buildScm(Project project, @SkipProjectScm boolean skip) {
        Optional<MavenScm> scm = project.getScm();
        if (!scm.isPresent()) {
            return absent();
        }
        return null;
    }

}
