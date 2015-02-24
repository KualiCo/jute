package org.kuali.common.jute.kfs;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.substringBetween;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.kfs.annotation.DepsContainer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public final class DepFragmentProvider implements Provider<String> {

    @Inject
    public DepFragmentProvider(@DepsContainer List<List<String>> container) {
        this.container = container;
    }

    private final List<List<String>> container;

    @Override
    public String get() {
        return Joiner.on('\n').join(transform(container, DependencyFunction.INSTANCE));
    }

    private enum DependencyFunction implements Function<List<String>, String> {
        INSTANCE;

        @Override
        public String apply(List<String> input) {
            List<String> list = newArrayList();
            String artifactId = null;
            String version = null;
            for (String line : input) {
                if (line.contains("<artifactId>")) {
                    artifactId = substringBetween(line, "<artifactId>", "</artifactId>");
                    list.add(line);
                } else {
                    if (line.contains("<version>")) {
                        version = substringBetween(line, "<version>", "</version>");
                        if (version.contains("${")) {
                            list.add(line);
                        } else {
                            String property = "${" + artifactId + ".version}";
                            list.add(replace(line, version, property));
                        }
                    } else {
                        list.add(line);
                    }
                }
            }
            return Joiner.on('\n').join(list);
        }
    }
}