package org.kuali.common.jute.kfs;

import static com.google.common.collect.Iterables.concat;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.base.BaseUnitTest;
import org.kuali.common.jute.kfs.annotation.Basedir;
import org.kuali.common.jute.kfs.annotation.DepVersions;

import com.google.common.base.Joiner;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class DepsTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createInjector(concat(getUnitTestModules(), asList(new KfsDepModule())));
            File basedir = injector.getInstance(Key.get(File.class, Basedir.class));
            List<String> versions = injector.getInstance(Key.get(new TypeLiteral<List<String>>() {}, DepVersions.class));
            info("basedir  -> %s", basedir);
            info("versions -> %s", versions.size());
            info("\n%s", Joiner.on('\n').join(versions));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
