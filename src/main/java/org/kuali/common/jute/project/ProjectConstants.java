package org.kuali.common.jute.project;

public final class ProjectConstants {

    private ProjectConstants() {}

    // These two must exactly match the entries in the pom
    private static final String GROUP_ID = "org.kuali.common";
    private static final String ARTIFACT_ID = "kuali-jute";

    public static final ProjectIdentifier KUALI_JUTE_PROJECT_ID = ProjectIdentifier.build(GROUP_ID, ARTIFACT_ID);

}
