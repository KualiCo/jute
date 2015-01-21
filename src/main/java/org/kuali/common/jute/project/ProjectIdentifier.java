package org.kuali.common.jute.project;

public interface ProjectIdentifier extends Comparable<ProjectIdentifier> {

    String getGroupId();

    String getArtifactId();

}
