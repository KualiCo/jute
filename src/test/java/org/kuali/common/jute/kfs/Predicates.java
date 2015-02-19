package org.kuali.common.jute.kfs;

import java.io.File;

import com.google.common.base.Predicate;

public final class Predicates {

    private Predicates() {}

    public static Predicate<File> javaFile() {
        return JavaFilePredicate.INSTANCE;
    }

    public static Predicate<File> childOf(File dir) {
        return new ChildPredicate(dir);
    }

    private static class ChildPredicate implements Predicate<File> {

        public ChildPredicate(File dir) {
            this.dir = dir;
        }

        private final File dir;

        @Override
        public boolean apply(File input) {
            return input.getAbsolutePath().startsWith(dir.getAbsolutePath());
        }

    }

    private enum JavaFilePredicate implements Predicate<File> {
        INSTANCE;

        @Override
        public boolean apply(File input) {
            return input.getPath().endsWith(".java");
        }
    }

}
