package org.kuali.common.jute.system;

import static com.google.common.collect.Lists.newArrayList;
import static java.io.File.pathSeparatorChar;
import static org.kuali.common.jute.base.Precondition.checkNotBlank;

import java.io.File;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.ImmutableList;

public final class SystemFiles {

    private static final Splitter SPLITTER = Splitter.on(pathSeparatorChar).omitEmptyStrings().trimResults();

    public static List<File> fromSystemProperty(StandardSystemProperty property) {
        String value = checkNotBlank(property.value(), property.key());
        List<File> files = newArrayList();
        Iterable<String> paths = SPLITTER.split(value);
        for (String path : paths) {
            File file = new File(path);
            files.add(file);
        }
        return ImmutableList.copyOf(files);
    }

}
