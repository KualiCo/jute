package org.kuali.common.jute.project.maven;

import static org.kuali.common.jute.project.UnitTestInjection.createUnitTestInjector;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.inject.Injector;

public class VersionProviderTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createUnitTestInjector();
            Project project = injector.getInstance(Project.class);
            info(project.getEncoding());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
