package org.kuali.common.jute.project.maven;

import static java.lang.System.currentTimeMillis;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.net.InetAddress.buildLocalHost;

import java.io.IOException;

import javax.inject.Provider;

import org.kuali.common.jute.net.InetAddress;
import org.kuali.common.jute.project.BuildEvent;
import org.kuali.common.jute.project.annotation.BuildHost;
import org.kuali.common.jute.project.annotation.BuildTimestamp;

import com.google.inject.AbstractModule;

public class CreateMetadataModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(BuildTimestamp.class).to(currentTimeMillis());
        bind(InetAddress.class).annotatedWith(BuildHost.class).toProvider(BuildHostProvider.INSTANCE);
        bind(BuildEvent.class).toProvider(BuildEvent.Builder.class);
        bind(ProjectMetadata.class).toProvider(ProjectMetadata.Builder.class);
        bind(Runnable.class).toProvider(CreateMetadataRunnable.Builder.class);
    }

    private enum BuildHostProvider implements Provider<InetAddress> {
        INSTANCE;

        @Override
        public InetAddress get() {
            try {
                return buildLocalHost();
            } catch (IOException e) {
                throw illegalState(e);
            }
        }

    }

}
