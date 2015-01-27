package org.kuali.common.jute.project.maven;

import static com.google.common.io.Resources.getResource;
import static org.kuali.common.jute.base.Exceptions.illegalState;
import static org.kuali.common.jute.base.Precondition.checkNotNull;
import static org.kuali.common.jute.project.maven.ProjectFunctions.metadataPathFunction;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.json.JsonService;

public final class MetadataProvider implements Provider<ProjectMetadata> {

    @Inject
    public MetadataProvider(ProjectIdentifier pid, JsonService json) {
        this.pid = checkNotNull(pid, "pid");
        this.json = checkNotNull(json, "json");
    }

    private final ProjectIdentifier pid;
    private final JsonService json;

    public static ProjectMetadata get(ProjectIdentifier pid, JsonService json) {
        return new MetadataProvider(pid, json).get();
    }

    @Override
    public ProjectMetadata get() {
        try {
            String path = metadataPathFunction().apply(pid);
            URL url = getResource(path);
            return json.read(url, ProjectMetadata.class);
        } catch (IOException e) {
            throw illegalState(e);
        }
    }

    public ProjectIdentifier getPid() {
        return pid;
    }

    public JsonService getJson() {
        return json;
    }

}
