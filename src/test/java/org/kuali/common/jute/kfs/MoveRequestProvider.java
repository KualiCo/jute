package org.kuali.common.jute.kfs;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.kuali.common.jute.kfs.Predicates.childOf;
import static org.kuali.common.jute.kfs.Predicates.javaFile;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kuali.common.jute.kfs.annotation.Files;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

public final class MoveRequestProvider implements Provider<List<MoveRequest>> {

    @Inject
    public MoveRequestProvider(@Files List<File> files, OldSrcDirs oldDirs, NewSrcDirs newDirs) {
        this.files = copyOf(files);
        this.oldDirs = oldDirs;
        this.newDirs = newDirs;
    }

    private final OldSrcDirs oldDirs;
    private final NewSrcDirs newDirs;
    private final ImmutableList<File> files;

    @Override
    public List<MoveRequest> get() {
        List<MoveRequest> list1 = split(oldDirs.getWork(), newDirs.getMain());
        List<MoveRequest> list2 = split(oldDirs.getUnit(), newDirs.getTest());
        List<MoveRequest> list3 = split(oldDirs.getInfrastructure(), newDirs.getTest());
        List<MoveRequest> list4 = split(oldDirs.getIntegration(), newDirs.getTest());
        return copyOf(concat(list1, list2, list3, list4));
    }

    private List<MoveRequest> split(File oldDir, DirPair pair) {
        List<File> source = copyOf(filter(files, and(javaFile(), childOf(oldDir))));
        List<File> resources = copyOf(filter(files, and(not(javaFile()), childOf(oldDir))));
        List<MoveRequest> list1 = copyOf(transform(source, new MoveRequestFunction(oldDir, pair.getSource())));
        List<MoveRequest> list2 = copyOf(transform(resources, new MoveRequestFunction(oldDir, pair.getResources())));
        return copyOf(concat(list1, list2));
    }

    private static class MoveRequestFunction implements Function<File, MoveRequest> {

        public MoveRequestFunction(File oldDir, File newDir) {
            this.oldDir = oldDir;
            this.newDir = newDir;
        }

        private final File oldDir;
        private final File newDir;

        @Override
        public MoveRequest apply(File input) {
            String path = removeStart(removeStart(input.getAbsolutePath(), oldDir.getAbsolutePath()), File.separator);
            File dst = new File(newDir, path);
            return MoveRequest.builder().withDst(dst).withSrc(input).build();
        }
    }

    public OldSrcDirs getOldDirs() {
        return oldDirs;
    }

    public NewSrcDirs getNewDirs() {
        return newDirs;
    }

    public List<File> getFiles() {
        return files;
    }

}
