package org.kuali.common.jute.project;

public final class ProjectConstants {

    private ProjectConstants() {}

    // These two must always exactly match what is in the pom.xml
    private static final String GROUP_ID = "org.kuali.common";
    private static final String ARTIFACT_ID = "kuali-jute";

    public static final ProjectIdentifier KUALI_JUTE_PROJECT_ID = DefaultProjectIdentifier.build(GROUP_ID, ARTIFACT_ID);

}
