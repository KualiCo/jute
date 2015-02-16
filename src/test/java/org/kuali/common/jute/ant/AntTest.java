package org.kuali.common.jute.ant;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Ordering.natural;
import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.substringsBetween;
import static org.kuali.common.jute.base.Strings.flatten;
import static org.kuali.common.jute.project.UnitTestInjection.getUnitTestModules;

import java.util.List;

import org.junit.Test;
import org.kuali.common.jute.ant.annotation.BuildFileContent;
import org.kuali.common.jute.base.BaseUnitTest;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import com.google.inject.Key;

public class AntTest extends BaseUnitTest {

    @Test
    public void test() {
        try {
            Injector injector = createInjector(concat(getUnitTestModules(), asList(new AntModule())));
            Key<String> key = Key.get(String.class, BuildFileContent.class);
            String content = injector.getInstance(key);
            String token = "<target";
            Function<String, String> transformer = new TokenFunction(token);
            List<String> sorted = natural().sortedCopy(transform(ImmutableList.copyOf(substringsBetween(content, token, token)), transformer));
            for (String element : sorted) {
                info(element);
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
