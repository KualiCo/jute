package org.kuali.common.jute.kfs;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.File;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public final class Functions {

    private Functions() {}

    public static Function<MoveRequest, String> gitMoveCommand(File basedir) {
        return new GitMoveCommandFunction(basedir);
    }

    public static Function<File, String> mkdirCommand(File basedir) {
        return new MkdirCommandFunction(basedir);
    }

    private static class MkdirCommandFunction implements Function<File, String> {

        public MkdirCommandFunction(File basedir) {
            this.basedir = basedir;
        }

        private final File basedir;

        @Override
        public String apply(File input) {
            return format("mkdir -p %s", removeStart(removeStart(input.getAbsolutePath(), basedir.getAbsolutePath()), File.separator));
        }
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
