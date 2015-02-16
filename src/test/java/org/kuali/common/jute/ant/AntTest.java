package org.kuali.common.jute.ant;

import static com.google.common.collect.Iterables.concat;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.ant.annotation.Targets;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class AntTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createInjector(concat(getUnitTestModules(), asList(new AntModule())));
            Key<List<Target>> key = Key.get(new TypeLiteral<List<Target>>() {}, Targets.class);
            List<Target> targets = injector.getInstance(key);
            for (Target target : targets) {
                info(target.getName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
