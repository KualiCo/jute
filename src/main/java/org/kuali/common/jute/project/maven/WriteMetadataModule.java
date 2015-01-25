package org.kuali.common.jute.project.maven;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.base.Exceptions.illegalState;

import java.io.IOException;

import org.kuali.common.jute.net.InetAddress;
import org.kuali.common.jute.project.BuildEvent;
import org.kuali.common.jute.project.BuildScm;
import org.kuali.common.jute.project.annotation.BuildHost;
import org.kuali.common.jute.project.annotation.BuildTimestamp;
import org.kuali.common.jute.project.maven.annotation.SkipProjectScm;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class WriteMetadataModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(BuildTimestamp.class).to(currentTimeMillis());
        bind(InetAddress.class).annotatedWith(BuildHost.class).toInstance(buildLocalHost());
        bind(new TypeLiteral<Optional<BuildScm>>() {}).toProvider(BuildScmProvider.class);
        bind(BuildEvent.class).toProvider(BuildEvent.Builder.class);
        bind(ProjectMetadata.class).toProvider(ProjectMetadata.Builder.class);
        bind(Runnable.class).toProvider(WriteMetadataRunnable.Builder.class);
    }

    @Provides
    @SkipProjectScm
    private boolean skipProjectScm(MavenEnvironment env) {
        return parseBoolean(env.getProperty("project.scm.skip", "true"));
    }

    private static InetAddress buildLocalHost() {
        try {
            return InetAddress.buildLocalHost();
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

}
