package org.kuali.common.jute.project.maven;

import static org.kuali.common.jute.project.UnitTestInjection.createUnitTestInjector;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.json.JsonService;

import com.google.inject.Injector;

public class VersionProviderTest extends BaseUnitTest {

    @Test
    public void test() {
        Injector injector = createUnitTestInjector();
        Project project = injector.getInstance(Project.class);
        VersionProvider provider = new VersionProvider(project);
        Version v = provider.get();
        JsonService json = injector.getInstance(JsonService.class);
        show(json, v);
    }

}
