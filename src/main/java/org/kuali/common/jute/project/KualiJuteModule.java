package org.kuali.common.jute.project;

import static org.kuali.common.jute.project.ProjectConstants.KUALI_JUTE_PROJECT_ID;

import com.google.inject.AbstractModule;

public class KualiJuteModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ProjectIdentifier.class).annotatedWith(KualiJute.class).toInstance(KUALI_JUTE_PROJECT_ID);
    }

}
