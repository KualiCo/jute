package org.kuali.common.jute.ant;

import static com.google.common.base.Functions.compose;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Ordering.natural;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.apache.commons.lang3.StringUtils.substringsBetween;
import static org.kuali.common.jute.base.Optionals.fromTrimToNull;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.base.Strings.flatten;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.ant.annotation.BuildFileContent;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;

public class TargetTokensProvider implements Provider<List<Target>> {

    @Inject
    public TargetTokensProvider(@BuildFileContent String content) {
        this.content = checkNotBlank(content, "content");
    }

    private final String content;

    @Override
    public List<Target> get() {
        String open = "<target";
        String close = ">";
        Function<String, Target> transformer = compose(TargetFunction.INSTANCE, new TokenFunction(open, close));
        return natural().sortedCopy(transform(copyOf(substringsBetween(content, open, close)), transformer));
    }

    public String getContent() {
        return content;
    }

    private enum TargetFunction implements Function<String, Target> {
        INSTANCE;

        Splitter splitter = Splitter.on(',').omitEmptyStrings().trimResults();

        @Override
        public Target apply(String input) {
            String fragment = substringBetween(input, "<target", ">");
            String name = substringBetween(fragment, "name=\"", "\"");
            Optional<String> iff = fromTrimToNull(substringBetween(input, "if=\"", "\""));
            Optional<String> unless = fromTrimToNull(substringBetween(input, "unless=\"", "\""));
            List<String> depends = splitter.splitToList(nullToEmpty(substringBetween(fragment, "depends=\"", "\"")));
            Target.Builder builder = Target.builder();
            builder.withName(name);
            builder.withDepends(depends);
            builder.withIff(iff);
            builder.withUnless(unless);
            return builder.build();
        }
    }

    private static class TokenFunction implements Function<String, String> {

        public TokenFunction(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        private final String prefix;
        private final String suffix;

        @Override
        public String apply(String input) {
            return flatten(prefix + input + suffix);
        }

    }

}
