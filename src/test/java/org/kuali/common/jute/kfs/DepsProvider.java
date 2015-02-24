package org.kuali.common.jute.kfs;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.kfs.annotation.PomContent;

import com.google.common.collect.ImmutableList;

public final class DepsProvider implements Provider<List<String>> {

    @Inject
    public DepsProvider(@PomContent List<String> lines) {
        this.lines = lines;
    }

    private final List<String> lines;

    @Override
    public List<String> get() {
        int fromIndex = -1;
        int toIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().equals("</build>")) {
                fromIndex = i + 1;
            }
            if (fromIndex != -1 && line.trim().equals("</dependencies>")) {
                toIndex = i;
            }
        }
        return ImmutableList.copyOf(lines.subList(fromIndex + 1, toIndex));
    }

}
