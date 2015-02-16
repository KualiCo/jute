package org.kuali.common.jute.ant;

import static com.google.common.collect.Iterables.concat;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.kuali.common.jute.base.Strings.flatten;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.ant.annotation.Targets;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.common.base.Function;
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

    private static class TokenFunction implements Function<String, String> {

        public TokenFunction(String prefix) {
            this.prefix = prefix;
        }

        private final String prefix;

        @Override
        public String apply(String input) {
            return flatten(prefix + input);
        }

    }

}
