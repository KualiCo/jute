package org.kuali.common.jute.project.maven;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkState;
import static org.kuali.common.jute.project.UnitTestInjection.createUnitTestInjector;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.json.JsonService;

public class VersionProviderTest extends BaseUnitTest {

    private JsonService json = createUnitTestInjector().getInstance(JsonService.class);

    @Test
    public void test() {
        checkState(new VersionProvider(build("1.0.0-SNAPSHOT")).get().isSnapshot());
        checkState(!(new VersionProvider(build("1.0.0")).get().isSnapshot()));
        show(json, new VersionProvider(build("1.0.0-beta-1")).get());
        show(json, new VersionProvider(build("1.0.0-beta-1-SNAPSHOT")).get());
    }

    protected Project build(String version) {
        ProjectCoordinates coords = ProjectCoordinates.build("org.kuali.common", "kuali-jute", version);
        return Project.builder().withCoordinates(coords).withEncoding(UTF_8.name()).build();
    }

}
