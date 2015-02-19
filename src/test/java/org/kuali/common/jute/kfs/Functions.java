package org.kuali.common.jute.kfs;

import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.File;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public final class Functions {

    private Functions() {}

    public static Function<MoveRequest, String> gitMoveCommand(File basedir) {
        return new GitMoveCommandFunction(basedir);
    }

    private static class GitMoveCommandFunction implements Function<MoveRequest, String> {

        public GitMoveCommandFunction(File basedir) {
            this.basedir = basedir;
        }

        private final File basedir;

        @Override
        public String apply(MoveRequest input) {
            String src = relativePath(basedir, input.getSrc());
            String dst = relativePath(basedir, input.getDst());
            return Joiner.on(' ').join("git", "mv", src, dst);
        }

        private String relativePath(File basedir, File file) {
            return removeStart(removeStart(file.getAbsolutePath(), basedir.getAbsolutePath()), File.separator);
        }

    }

}
