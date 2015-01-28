package org.kuali.common.jute.project.maven;

import static com.google.common.base.Optional.absent;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.endsWithIgnoreCase;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;

public final class VersionProvider implements Provider<Version> {

    @Inject
    public VersionProvider(Project project) {
        this.project = checkNotNull(project, "project");
    }

    private final Project project;

    @Override
    public Version get() {

        // extract the version text
        String text = project.getCoordinates().getVersion();

        // extract version number properties from the text
        boolean snapshot = endsWithIgnoreCase(text, "-SNAPSHOT");
        List<String> tokens = Splitter.on('.').splitToList(text);
        Iterator<String> itr = tokens.iterator();
        int major = parseInt(itr.next());
        int minor = parseInt(itr.next());
        String remainder = itr.next();
        List<String> remainderTokens = Splitter.on('-').splitToList(remainder);
        Iterator<String> remainderIterator = remainderTokens.iterator();
        int patch = parseInt(remainderIterator.next());
        Optional<String> qualifier = getQualifier(remainderIterator);

        // store the version number info into an immutable object
        Version.Builder builder = Version.builder();
        builder.withMajor(major);
        builder.withMinor(minor);
        builder.withPatch(patch);
        builder.withQualifier(qualifier);
        builder.withSnapshot(snapshot);
        return builder.build();
    }

    private Optional<String> getQualifier(Iterator<String> itr) {
        StringBuilder sb = new StringBuilder();
        while (itr.hasNext()) {
            sb.append(itr.next());
            sb.append('-');
        }
        if (sb.length() == 0) {
            return absent();
        } else {
            return Optional.of(removeEnd(sb.toString(), "-"));
        }
    }

    public Project getProject() {
        return project;
    }

}
