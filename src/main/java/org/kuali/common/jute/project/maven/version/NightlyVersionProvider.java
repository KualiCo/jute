package org.kuali.common.jute.project.maven.version;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.kuali.common.jute.base.Exceptions.illegalArgument;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.project.maven.Maven.SNAPSHOT_QUALIFIER;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.project.maven.ProjectCoordinates;
import org.kuali.common.jute.project.maven.ProjectMetadata;

public final class NightlyVersionProvider implements Provider<String> {

    @Inject
    public NightlyVersionProvider(ProjectMetadata metadata) {
        this.metadata = checkNotNull(metadata, "metadata");
    }

    private final ProjectMetadata metadata;

    @Override
    public String get() {
        checkState(metadata.getBuild().getScm().isPresent(), "scm info is required for a nightly build version");
        ProjectCoordinates coords = metadata.getProject().getCoordinates();
        String version = removeEnd(coords.getVersion(), SNAPSHOT_QUALIFIER);
        return version + "-" + getScmQualifier(metadata.getBuild().getScm().get());
    }

    private String getScmQualifier(BuildScm scm) {
        switch (scm.getType()) {
            case GIT:
                // Prefix the revision with the current date so sorting works correctly.
                // Git revision's are a 128 bit hash, so sorting would not work otherwise.
                // For the "normal" use case, where builds are done once per day, things will be a-ok.
                // However, if more than one build is done per day, sorting isn't going to work.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(new Date(currentTimeMillis()));
                return day + "-r" + left(scm.getRevision(), 7);
            case SVN:
                // Subversion uses a simple, incrementing, number. Works well with sorting
                return "r" + scm.getRevision();
            default:
                throw illegalArgument("%s is unknown", scm.getType());
        }
    }

}
