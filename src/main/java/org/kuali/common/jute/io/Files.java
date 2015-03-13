package org.kuali.common.jute.io;

import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.io.File;

import com.google.common.base.Predicate;

public final class Files {

    private Files() {}

    public static String relativePath(File parent, File child) {
        checkNotNull(parent, "parent");
        checkNotNull(child, "child");
        checkState(childOf(parent).apply(child), "%s is not a child of %s", child, parent);
        return removeStart(removeStart(child.getAbsolutePath(), parent.getAbsolutePath()), File.separator);
    }

    public static Predicate<File> childOf(File dir) {
        return new ChildOfPredicate(dir);
    }

    private static class ChildOfPredicate implements Predicate<File> {

        private ChildOfPredicate(File dir) {
            this.dir = checkNotNull(dir, "dir");
        }

        private final File dir;

        @Override
        public boolean apply(File input) {
            checkNotNull(input, "input");
            return startsWith(input.getAbsolutePath(), dir.getAbsolutePath());
        }
    }

}
