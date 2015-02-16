package org.kuali.common.jute.ant;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Ordering.natural;
import static org.apache.commons.lang3.StringUtils.substringsBetween;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;
import static org.kuali.common.jute.base.Strings.flatten;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.ant.annotation.BuildFileContent;

import com.google.common.base.Function;

public class TargetTokensProvider implements Provider<List<String>> {

    @Inject
    public TargetTokensProvider(@BuildFileContent String content) {
        this.content = checkNotBlank(content, "content");
    }

    private final String content;

    @Override
    public List<String> get() {
        String token = "<target";
        Function<String, String> transformer = new TokenFunction(token);
        return natural().sortedCopy(transform(copyOf(substringsBetween(content, token, token)), transformer));
    }

    public String getContent() {
        return content;
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
