package org.kuali.common.jute.project.maven;

import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.StringUtils.left;
import static org.kuali.common.jute.base.Exceptions.illegalArgument;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.project.BuildScm;

public final class NightlyVersionProvider implements Provider<String> {

    @Inject
    public NightlyVersionProvider(Version version, BuildScm scm) {
        this.version = checkNotNull(version, "version");
        this.scm = checkNotNull(scm, "scm");
    }

    private final Version version;
    private final BuildScm scm;

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append(version.getMajor() + "");
        sb.append(".");
        sb.append(version.getMinor() + "");
        sb.append(".");
        sb.append(version.getPatch() + "");
        sb.append(".");
        if (version.getQualifier().isPresent()) {
            sb.append("-");
            sb.append(version.getQualifier().get());
        }
        sb.append("-");
        sb.append(getQualifier(scm));
        return sb.toString();
    }

    private String getQualifier(BuildScm scm) {
        switch (scm.getType()) {
            case GIT:
                // Pre-pending the revision with the current date so sorting works correctly
                // Git revision's are a 128 bit hash, so sorting would not work otherwise
                // Even with this, 2 builds done on the same day, will not sort correctly
                // For the "normal" use case, where builds are done once per day, we should be good to go
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(new Date(currentTimeMillis()));
                return day + "-r" + left(scm.getRevision(), 8);
            case SVN:
                // Subversion uses a simple, incrementing, number. Sorting is going to work correctly out of the box
                return "r" + scm.getRevision();
            default:
                throw illegalArgument("%s is unknown", scm.getType());
        }
    }

    public Version getVersion() {
        return version;
    }

    public BuildScm getScm() {
        return scm;
    }

}