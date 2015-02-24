package org.kuali.common.jute.kfs;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.kfs.annotation.Deps;

public final class DepsContainer implements Provider<List<List<String>>> {

    @Inject
    public DepsContainer(@Deps List<String> lines) {
        this.lines = lines;
    }

    private final List<String> lines;

    @Override
    public List<List<String>> get() {
        List<Indices> list = getIndices(lines);
        List<List<String>> container = newArrayList();
        for (Indices element : list) {
            List<String> sub = lines.subList(element.getFrom(), element.getTo());
            container.add(sub);
        }
        return container;
    }

    private List<Indices> getIndices(List<String> lines) {
        List<Indices> list = newArrayList();
        int from = -1;
        int to = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().equals("<dependency>")) {
                from = i;
            }
            if (line.trim().equals("</dependency>")) {
                to = i;
                Indices element = new Indices(from, to + 1);
                list.add(element);
                from = -1;
                to = -1;
            }
        }
        return copyOf(list);
    }

    private static class Indices {

        public Indices(int from, int to) {
            this.from = from;
            this.to = to;
        }

        private final int from;
        private final int to;

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }
    }

}
